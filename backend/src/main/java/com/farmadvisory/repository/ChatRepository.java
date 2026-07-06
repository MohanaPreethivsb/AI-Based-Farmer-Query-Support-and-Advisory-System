package com.farmadvisory.repository;

import com.farmadvisory.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<Chat> findByIdAndUserId(String id, String userId);
    void deleteByIdAndUserId(String id, String userId);
}
