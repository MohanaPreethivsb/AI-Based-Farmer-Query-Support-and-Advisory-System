package com.farmadvisory.service;

import com.farmadvisory.client.FreeFarmAIClient;
import com.farmadvisory.dto.ChatDTO;
import com.farmadvisory.entity.Chat;
import com.farmadvisory.exception.BadRequestException;
import com.farmadvisory.exception.ResourceNotFoundException;
import com.farmadvisory.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final FreeFarmAIClient freeFarmAIClient;

    public ChatDTO askQuestion(String userId, String question) {
        // Validate question
        if (question == null || question.trim().length() < 5) {
            throw new BadRequestException("Question must be at least 5 characters");
        }

        if (question.length() > 1000) {
            throw new BadRequestException("Question cannot exceed 1000 characters");
        }

        // Get AI response
        String response = freeFarmAIClient.getAIResponse(question);

        // Save to database
        Chat chat = Chat.builder()
                .userId(userId)
                .question(question)
                .response(response)
                .build();

        chat = chatRepository.save(chat);

        return mapToDTO(chat);
    }

    public List<ChatDTO> getChatHistory(String userId, String search) {
        List<Chat> chats;

        if (search != null && !search.trim().isEmpty()) {
            // This would require custom query methods in repository
            // For simplicity, we'll do filtering in Java
            String searchLower = search.toLowerCase();
            chats = chatRepository.findByUserIdOrderByCreatedAtDesc(userId)
                    .stream()
                    .filter(chat -> chat.getQuestion().toLowerCase().contains(searchLower) ||
                            chat.getResponse().toLowerCase().contains(searchLower))
                    .collect(Collectors.toList());
        } else {
            chats = chatRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }

        return chats.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteChat(String chatId, String userId) {
        Chat chat = chatRepository.findByIdAndUserId(chatId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));

        chatRepository.deleteByIdAndUserId(chatId, userId);
    }

    private ChatDTO mapToDTO(Chat chat) {
        return ChatDTO.builder()
                ._id(chat.getId())
                .id(chat.getId())
                .question(chat.getQuestion())
                .response(chat.getResponse())
                .createdAt(chat.getCreatedAt())
                .build();
    }
}
