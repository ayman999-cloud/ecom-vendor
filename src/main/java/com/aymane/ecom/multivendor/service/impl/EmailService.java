package com.aymane.ecom.multivendor.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationOtpEmail(final String userEmail,
                                         final String otp,
                                         final String subject,
                                         final String text) {
        try {
            final MimeMessage message = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setSubject(subject);
            helper.setText(text);
            helper.setTo(userEmail);
            mailSender.send(message);
        } catch (final Exception e) {
            throw new MailSendException("Failed to send email", e.getCause());
        }

    }
}
