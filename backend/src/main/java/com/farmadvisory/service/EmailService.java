package com.farmadvisory.service;

import com.farmadvisory.exception.EmailDeliveryException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final Environment environment;

    @Value("${spring.mail.username:}")
    private String fromEmail;

    @Value("${spring.mail.password:}")
    private String mailPassword;

    @Value("${app.email.dev-otp-fallback:false}")
    private boolean devOtpFallback;

    public String sendPasswordResetOtp(String toEmail, String otp) {
        if (!isMailConfigured()) {
            return handleEmailUnavailable(toEmail, otp, "SMTP email is not configured");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail.trim());
            message.setTo(toEmail);
            message.setSubject("Farm Advisory Password Reset OTP");
            message.setText(buildOtpMessage(otp));

            mailSender.send(message);
            log.info("Password reset OTP email sent to {}", toEmail);
            return "OTP sent successfully to your registered email";
        } catch (MailException ex) {
            log.error("Failed to send password reset OTP email to {}", toEmail, ex);
            if (isDevelopmentFallbackEnabled()) {
                return devOtpMessage(toEmail, otp, "Email delivery failed");
            }
            throw new EmailDeliveryException("Unable to send OTP email. Please try again later.", ex);
        }
    }

    private boolean isMailConfigured() {
        return hasText(fromEmail) && hasText(mailPassword);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String handleEmailUnavailable(String toEmail, String otp, String reason) {
        if (isDevelopmentFallbackEnabled()) {
            return devOtpMessage(toEmail, otp, reason);
        }

        throw new EmailDeliveryException(
                "Unable to send OTP email. Please configure Gmail SMTP username and app password.",
                null
        );
    }

    private boolean isDevelopmentFallbackEnabled() {
        return devOtpFallback && !environment.matchesProfiles("prod");
    }

    private String devOtpMessage(String toEmail, String otp, String reason) {
        log.warn("{} for {}. Development password reset OTP: {}", reason, toEmail, otp);
        return "Email is not configured for this local app. Use this development OTP: " + otp;
    }

    private String buildOtpMessage(String otp) {
        return "Hello,\n\n"
                + "Your password reset OTP for Farmer Advisory System is: " + otp + "\n\n"
                + "This OTP is valid for 5 minutes. Do not share it with anyone.\n\n"
                + "Regards,\n"
                + "Farmer Advisory System";
    }
}
