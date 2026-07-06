package com.farmadvisory.controller;

import com.farmadvisory.dto.ChatDTO;
import com.farmadvisory.dto.ChatRequest;
import com.farmadvisory.security.JwtAuthenticationToken;
import com.farmadvisory.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> askQuestion(
            Authentication authentication,
            @Valid @RequestBody ChatRequest request) {
        String userId = extractUserId(authentication);
        ChatDTO chatDTO = chatService.askQuestion(userId, request.getQuestion());

        Map<String, Object> response = new HashMap<>();
        response.put("chat", chatDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getChatHistory(
            Authentication authentication,
            @RequestParam(value = "search", required = false) String search) {
        String userId = extractUserId(authentication);
        List<ChatDTO> chats = chatService.getChatHistory(userId, search);

        Map<String, Object> response = new HashMap<>();
        response.put("chats", chats);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteChat(
            Authentication authentication,
            @PathVariable String id) {
        String userId = extractUserId(authentication);
        chatService.deleteChat(id, userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Chat deleted successfully");
        return ResponseEntity.ok(response);
    }

    private String extractUserId(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) authentication).getUserId();
        }
        throw new IllegalArgumentException("Invalid authentication");
    }
}
