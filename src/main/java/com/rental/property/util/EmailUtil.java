package com.rental.property.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {

    private static final String OTP_SUBJECT = "Password Reset OTP From RentHub Team";
    private static final String OTP_MESSAGE_TEMPLATE = """
        Dear User,

        Your One-Time Password (OTP) for resetting your password is: %s
        This OTP is valid for 10 minutes. Please do not share it with anyone.

        If you did not request a password reset, please ignore this email or contact support.

        Best regards,
        RentHUB Team
        """;

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.from-address:no-reply@rentalproperty.com}")
    private String fromEmail;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(OTP_SUBJECT);
        message.setText(String.format(OTP_MESSAGE_TEMPLATE, otp));
        javaMailSender.send(message);
    }
}