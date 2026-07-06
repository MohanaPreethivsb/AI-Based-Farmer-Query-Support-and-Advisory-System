package com.farmadvisory.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "crops")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Crop {

    @Id
    private String id;

    private String userId;

    private String name;

    private String variety;

    private String season;

    @Builder.Default
    private String status = "planned";

    private Double area;

    @Builder.Default
    private String areaUnit = "acre";

    private LocalDate sowingDate;

    private LocalDate expectedHarvestDate;

    private String notes;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
