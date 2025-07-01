package com.hitta.ContractApp.service;

import com.hitta.ContractApp.dtos.AuthResponse;
import com.hitta.ContractApp.model.Token;
import com.hitta.ContractApp.model.Users;
import com.hitta.ContractApp.repo.TokenRepo;
import org.springframework.http.ResponseCookie;
import java.time.Duration;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TokenService {

    private final JwtService jwtService;
    private final TokenRepo tokenRepo;

    public TokenService(
            JwtService jwtService,
            TokenRepo tokenRepo
    ){
        this.jwtService = jwtService;
        this.tokenRepo = tokenRepo;
    }

    public AuthResponse createAccessAndRefreshTokens(Users user){
        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = createOrUpdateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String createOrUpdateRefreshToken(Users user) {
        Optional<Token> optionalToken = tokenRepo.findByUser(user);

        var tokenValue = jwtService.generateRefreshToken();
        Token token;

        if (optionalToken.isPresent()) {
            token = optionalToken.get();
            token.setToken(tokenValue);
            token.setCreatedAt(LocalDateTime.now());
            token.setExpiresAt(LocalDateTime.now().plusDays(7));
            token.setRevoked(false);
        } else {
            token = Token.builder()
                    .token(tokenValue)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .user(user)
                    .revoked(false)
                    .build();
        }

        tokenRepo.save(token);
        return tokenValue;
    }



    public void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofDays(14))
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ZERO)
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}

