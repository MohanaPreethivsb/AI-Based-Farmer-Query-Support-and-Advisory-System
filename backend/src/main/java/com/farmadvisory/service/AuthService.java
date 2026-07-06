package com.farmadvisory.service;

import com.farmadvisory.dto.AuthRequest;
import com.farmadvisory.dto.AuthResponse;
import com.farmadvisory.dto.ForgotPasswordRequest;
import com.farmadvisory.dto.ResetPasswordRequest;
import com.farmadvisory.dto.UserDTO;
import com.farmadvisory.dto.VerifyOtpRequest;
import com.farmadvisory.entity.User;
import com.farmadvisory.exception.BadRequestException;
import com.farmadvisory.exception.UnauthorizedException;
import com.farmadvisory.repository.UserRepository;
import com.farmadvisory.security.JwtTokenProvider;
import com.farmadvisory.util.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final OtpService otpService;

    public AuthResponse register(AuthRequest request) {
        String email = normalizeEmail(request.getEmail());

        // Check if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new BadRequestException("An account with this email already exists");
        }

        // Validate required fields
        if (request.getName() == null || request.getName().trim().length() < 2) {
            throw new BadRequestException("Name must be at least 2 characters");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters");
        }

        // Create new user
        User user = User.builder()
                .name(request.getName())
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone() != null ? request.getPhone() : "")
                .district(request.getDistrict() != null ? request.getDistrict() : "")
                .role("farmer")
                .build();

        user = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getId());
        UserDTO userDTO = userMapper.toDTO(user);

        return AuthResponse.builder()
                .token(token)
                .user(userDTO)
                .build();
    }

    public AuthResponse login(AuthRequest request) {
        String email = normalizeEmail(request.getEmail());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid email or password");
        }

        String token = jwtTokenProvider.generateToken(user.getId());
        UserDTO userDTO = userMapper.toDTO(user);

        return AuthResponse.builder()
                .token(token)
                .user(userDTO)
                .build();
    }

    public String forgotPassword(ForgotPasswordRequest request) {
        String email = normalizeEmail(request.getEmail());

        userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("No account found with this email"));

        String otp = otpService.generateOtp(email);
        String message = emailService.sendPasswordResetOtp(email, otp);
        log.info("Password reset OTP requested for {}", email);

        return message;
    }

    public String verifyOtp(VerifyOtpRequest request) {
        String email = normalizeEmail(request.getEmail());

        userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("No account found with this email"));

        boolean verified = otpService.verifyOtp(email, request.getOtp());
        if (!verified) {
            throw new BadRequestException("Invalid or expired OTP");
        }

        return "OTP verified successfully";
    }

    public String resetPassword(ResetPasswordRequest request) {
        String email = normalizeEmail(request.getEmail());

        if (request.getNewPassword() == null || request.getNewPassword().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters");
        }

        otpService.validateOtpVerified(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("No account found with this email"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        otpService.clearOtp(email);

        log.info("Password reset completed for {}", email);
        return "Password reset successfully";
    }

    public UserDTO getProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        return userMapper.toDTO(user);
    }

    public AuthResponse updateProfile(String userId, AuthRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (request.getName() != null && !request.getName().isEmpty()) {
            if (request.getName().length() < 2) {
                throw new BadRequestException("Name must be at least 2 characters");
            }
            user.setName(request.getName());
        }

        if (request.getPhone() != null && !request.getPhone().isEmpty()) {
            if (request.getPhone().length() < 7) {
                throw new BadRequestException("Phone number is too short");
            }
            user.setPhone(request.getPhone());
        }

        if (request.getDistrict() != null && !request.getDistrict().isEmpty()) {
            user.setDistrict(request.getDistrict());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            if (request.getPassword().length() < 6) {
                throw new BadRequestException("Password must be at least 6 characters");
            }
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        user = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getId());
        UserDTO userDTO = userMapper.toDTO(user);

        return AuthResponse.builder()
                .token(token)
                .user(userDTO)
                .build();
    }

    private String normalizeEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException("Email is required");
        }

        return email.trim().toLowerCase();
    }
}
