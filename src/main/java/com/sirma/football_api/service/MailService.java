package com.sirma.football_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String to, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password reset request");
        message.setText("You requested to reset your password.\n\n"
                + "Click the link below to choose a new password:\n"
                + resetLink + "\n\n"
                + "If you did not request this, you can ignore this email.");
        try {
            mailSender.send(message);
            log.info("Sent password reset email to {}", to);
        } catch (Exception ex) {
            log.error("Failed to send password reset email to {}", to, ex);
        }
    }
}

