package com.hitta.ContractApp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender emailSender;

    public EmailService(
            JavaMailSender emailSender
    ){
        this.emailSender = emailSender;
    }

    @Async("taskExecutor")
    public void sendEmail(String to, String subject, String text){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }


        try{
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            emailSender.send(message);
        } catch (MessagingException e){
            System.out.println(e.getMessage());
        }

    }
}
