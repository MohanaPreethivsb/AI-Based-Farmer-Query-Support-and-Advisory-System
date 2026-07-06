package com.farmadvisory.controller;

import com.farmadvisory.dto.AuthRequest;
import com.farmadvisory.dto.AuthResponse;
import com.farmadvisory.dto.ForgotPasswordRequest;
import com.farmadvisory.dto.ResetPasswordRequest;
import com.farmadvisory.dto.UserDTO;
import com.farmadvisory.dto.VerifyOtpRequest;
import com.farmadvisory.security.JwtAuthenticationToken;
import com.farmadvisory.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/api/auth", "/auth"})
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<java.util.Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        String message = authService.forgotPassword(request);
        return ResponseEntity.ok(java.util.Map.of("message", message));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<java.util.Map<String, String>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {
        String message = authService.verifyOtp(request);
        return ResponseEntity.ok(java.util.Map.of("message", message));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<java.util.Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        String message = authService.resetPassword(request);
        return ResponseEntity.ok(java.util.Map.of("message", message));
    }

    @GetMapping("/profile")
    public ResponseEntity<java.util.Map<String, UserDTO>> getProfile(Authentication authentication) {
        String userId = extractUserId(authentication);
        UserDTO userDTO = authService.getProfile(userId);
        return ResponseEntity.ok(java.util.Map.of("user", userDTO));
    }

    @PutMapping("/profile")
    public ResponseEntity<AuthResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody AuthRequest request) {
        String userId = extractUserId(authentication);
        AuthResponse response = authService.updateProfile(userId, request);
        return ResponseEntity.ok(response);
    }

    private String extractUserId(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getUserId();
        }
        throw new IllegalArgumentException("Invalid authentication");
    }
}
