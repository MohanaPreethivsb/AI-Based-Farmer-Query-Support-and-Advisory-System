package com.farmadvisory.service;

import com.farmadvisory.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class OtpService {

    private static final Duration OTP_VALIDITY = Duration.ofMinutes(5);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final Map<String, OtpDetails> otpStorage = new ConcurrentHashMap<>();

    public String generateOtp(String email) {
        String otp = String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
        Instant expiryTime = Instant.now().plus(OTP_VALIDITY);

        otpStorage.put(email, new OtpDetails(otp, expiryTime, false));
        log.debug("Generated password reset OTP for {}. It expires at {}", email, expiryTime);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        OtpDetails details = otpStorage.get(email);

        if (details == null) {
            log.warn("OTP verification failed for {} because no OTP was found", email);
            return false;
        }

        if (details.isExpired()) {
            otpStorage.remove(email);
            log.warn("OTP verification failed for {} because OTP expired", email);
            return false;
        }

        if (!details.otp().equals(otp)) {
            log.warn("OTP verification failed for {} because OTP did not match", email);
            return false;
        }

        otpStorage.put(email, details.markVerified());
        log.info("Password reset OTP verified for {}", email);
        return true;
    }

    public void validateOtpVerified(String email) {
        OtpDetails details = otpStorage.get(email);

        if (details == null || details.isExpired()) {
            otpStorage.remove(email);
            throw new BadRequestException("OTP has expired or was not requested");
        }

        if (!details.verified()) {
            throw new BadRequestException("Please verify OTP before resetting password");
        }
    }

    public void clearOtp(String email) {
        otpStorage.remove(email);
    }

    private record OtpDetails(String otp, Instant expiryTime, boolean verified) {

        private boolean isExpired() {
            return Instant.now().isAfter(expiryTime);
        }

        private OtpDetails markVerified() {
            return new OtpDetails(otp, expiryTime, true);
        }
    }
}
