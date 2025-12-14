package com.hitta.ContractApp.service;

import com.hitta.ContractApp.exceptions.AlreadyVerifiedException;
import com.hitta.ContractApp.exceptions.ExpiredTokenException;
import com.hitta.ContractApp.exceptions.InvalidTokenException;
import com.hitta.ContractApp.exceptions.ResourceNotFoundException;
import com.hitta.ContractApp.model.Users;
import com.hitta.ContractApp.model.VerificationToken;
import com.hitta.ContractApp.repo.UserRepo;
import com.hitta.ContractApp.repo.VerificationTokenRepo;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationService {

    private final VerificationTokenRepo verificationTokenRepo;
    private final TokenService tokenService;
    private final EmailService emailService;
    private final UserRepo userRepo;

    @Value("${FRONTEND_URL}")
    private String frontUrl;

    public VerificationService(
            VerificationTokenRepo verificationTokenRepo,
            TokenService tokenService,
            EmailService emailService,
            UserRepo userRepo) {
        this.verificationTokenRepo = verificationTokenRepo;
        this.tokenService = tokenService;
        this.emailService = emailService;
        this.userRepo = userRepo;
    }

    @Transactional
    public void sendVerificationEmail(Users user) {
        try{
            VerificationToken token = new VerificationToken(user, 15);
            VerificationToken savedToken = verificationTokenRepo.save(token);
            sendEmail(user, savedToken);
        }catch(RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    @Transactional
    public void resendVerificationEmail(String token) {
        try{
            VerificationToken oldToken = verificationTokenRepo.findByToken(token).orElseThrow(() -> new InvalidTokenException("Token doesn't exist"));
            Users user = oldToken.getUser();

            VerificationToken savedToken = verificationTokenRepo.save(new VerificationToken(user, 15));
            sendEmail(user, savedToken);
        }catch(RuntimeException e){
            System.out.println(e.getMessage());
        }
    }

    private void sendEmail(Users user, VerificationToken token){
        String baseUrl = frontUrl + "/account";
        String verifyLink = baseUrl + "/verify?token=" + token.getToken();
        String deleteLink = baseUrl + "/delete?token=" + token.getToken();

        String subject = "Verify Your Account";
        String html =
                "<!DOCTYPE html>"
                        + "<html lang='en'>"
                        + "<head>"
                        + "  <meta charset='UTF-8' />"
                        + "  <title>Verify Your Account</title>"
                        + "</head>"
                        + "<body style='font-family: Arial, sans-serif; font-size: 16px; color: #333; background-color: #f9f9f9; padding: 24px;'>"
                        + "  <div style='max-width: 600px; margin: auto; background-color: #ffffff; padding: 32px; border-radius: 8px; border: 1px solid #e0e0e0;'>"
                        + "    <h2 style='color: #2c3e50; margin-top: 0'>Welcome to Real</h2>"
                        + "    <p>Thank you for signing up. To start using your account, please verify your email address by clicking the button below:</p>"
                        + "    <div style='margin: 30px 0'>"
                        + "      <a href='" + verifyLink + "' "
                        + "         style='display: inline-block; background-color: #1a73e8; color: white; padding: 14px 28px; text-decoration: none; border-radius: 4px; font-weight: 600; font-size: 16px;'>"
                        + "        Verify Account"
                        + "      </a>"
                        + "    </div>"
                        + "    <hr style='border: none; border-top: 1px solid #ddd; margin: 40px 0' />"
                        + "    <p style='color: #555'>"
                        + "      If you did not create this account or prefer not to proceed, you can choose to delete your account using the following link:"
                        + "    </p>"
                        + "    <p style='margin-top: 8px'>"
                        + "      <a href='" + deleteLink + "' style='color: #414141; text-decoration: underline'>"
                        + "        " + deleteLink
                        + "      </a>"
                        + "    </p>"
                        + "    <p style='font-size: 14px; color: #888; margin-top: 40px'>"
                        + "      This link will expire in 15 minutes for your security."
                        + "    </p>"
                        + "  </div>"
                        + "</body>"
                        + "</html>";



        emailService.sendEmail(user.getEmail(), subject, html);
    }


    @Transactional
    public void verifyAccountWithToken(HttpServletResponse response, String token) {
        System.out.println("Verifying token: " + token);
        VerificationToken vt = verificationTokenRepo.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        Users user = vt.getUser();

        if (vt.isExpired()) {
            verificationTokenRepo.deleteByToken(vt.getToken());
            throw new ExpiredTokenException("Token expired");
        }


        if (user.isEmailVerified()) {
            verificationTokenRepo.deleteByToken(vt.getToken());
            throw new AlreadyVerifiedException("Account already verified.");
        }


        user.setEmailVerified(true);
        userRepo.save(user);

        vt.setLastSentAt(LocalDateTime.now());
        verificationTokenRepo.save(vt);
        tokenService.createOrUpdateRefreshToken(response, user);
    }


    @Transactional
    public void deleteAccountWithToken(HttpServletResponse response, String token) {
        VerificationToken vt = verificationTokenRepo.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        if (vt.isExpired()) {
            verificationTokenRepo.delete(vt);
            throw new ExpiredTokenException("Token expired");
        }

        Users user = userRepo.findById(vt.getUser().getId()).orElseThrow(() -> new ResourceNotFoundException("Verification Token has no user"));
        userRepo.delete(user);
        tokenService.deleteRefreshTokenCookie(response);
    }
}
