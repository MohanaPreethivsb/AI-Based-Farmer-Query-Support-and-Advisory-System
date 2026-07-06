package com.farmadvisory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CropDTO {
    private String _id;
    private String id;
    private String name;
    private String variety;
    private String season;
    private String status;
    private Double area;
    private String areaUnit;
    private LocalDate sowingDate;
    private LocalDate expectedHarvestDate;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
