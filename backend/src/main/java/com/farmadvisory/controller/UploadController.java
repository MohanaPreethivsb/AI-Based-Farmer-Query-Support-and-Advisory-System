package com.farmadvisory.controller;

import com.farmadvisory.dto.ImageDTO;
import com.farmadvisory.dto.WeatherResponse;
import com.farmadvisory.security.JwtAuthenticationToken;
import com.farmadvisory.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadImage(
            Authentication authentication,
            @RequestParam("image") MultipartFile file) throws IOException {
        String userId = extractUserId(authentication);
        ImageDTO imageDTO = uploadService.uploadImage(userId, file);

        Map<String, Object> response = new HashMap<>();
        response.put("image", imageDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getUploadedImages(Authentication authentication) {
        String userId = extractUserId(authentication);
        List<ImageDTO> images = uploadService.getUploadedImages(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("images", images);
        return ResponseEntity.ok(response);
    }

    private String extractUserId(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getUserId();
        }
        throw new IllegalArgumentException("Invalid authentication");
    }
}
