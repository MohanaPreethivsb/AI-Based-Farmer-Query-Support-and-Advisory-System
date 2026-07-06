package com.farmadvisory.controller;

import com.farmadvisory.dto.CropDTO;
import com.farmadvisory.security.JwtAuthenticationToken;
import com.farmadvisory.service.CropService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/crops")
@RequiredArgsConstructor
public class CropController {

    private final CropService cropService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCrops(Authentication authentication) {
        return ResponseEntity.ok(Map.of("crops", cropService.getCrops(extractUserId(authentication))));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCrop(
            Authentication authentication,
            @RequestBody CropDTO request) {
        CropDTO crop = cropService.createCrop(extractUserId(authentication), request);
        return new ResponseEntity<>(Map.of("crop", crop), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCrop(
            Authentication authentication,
            @PathVariable String id,
            @RequestBody CropDTO request) {
        CropDTO crop = cropService.updateCrop(extractUserId(authentication), id, request);
        return ResponseEntity.ok(Map.of("crop", crop));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCrop(
            Authentication authentication,
            @PathVariable String id) {
        cropService.deleteCrop(extractUserId(authentication), id);
        return ResponseEntity.ok(Map.of("message", "Crop deleted successfully"));
    }

    private String extractUserId(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getUserId();
        }
        throw new IllegalArgumentException("Invalid authentication");
    }
}
