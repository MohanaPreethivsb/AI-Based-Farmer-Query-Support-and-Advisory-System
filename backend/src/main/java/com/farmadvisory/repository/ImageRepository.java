package com.farmadvisory.repository;

import com.farmadvisory.entity.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {
    List<Image> findByUserIdOrderByCreatedAtDesc(String userId);
}
