package com.farmadvisory.repository;

import com.farmadvisory.entity.Crop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CropRepository extends MongoRepository<Crop, String> {
    List<Crop> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<Crop> findByIdAndUserId(String id, String userId);
    void deleteByIdAndUserId(String id, String userId);
}
