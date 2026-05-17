package com.internshiproject.portfolioManagement.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    // ===================== OTP MAIL =====================
    public void sendOtp(String toEmail, String otp) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("OTP Verification");
            message.setText("Your OTP is: " + otp);

            mailSender.send(message);

            logger.info("OTP email sent to {}", toEmail);

        } catch (Exception e) {
            logger.error("Error sending OTP email", e);
        }
    }

    // ===================== SIMPLE TEXT MAIL =====================
    public void sendMail(String toEmail, String subject, String body) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);

            logger.info("Simple email sent to {}", toEmail);

        } catch (Exception e) {
            logger.error("Error sending simple email", e);
        }
    }

    // ===================== HTML MAIL (FOR ALERTS) =====================
    public void sendHtmlMail(String to, String subject, String htmlBody) {

        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); //  HTML enabled

            mailSender.send(message);

            logger.info("HTML email sent to {}", to);

        } catch (MessagingException | MailException e) {
            logger.error("Error sending HTML email", e);
        }
    }
}