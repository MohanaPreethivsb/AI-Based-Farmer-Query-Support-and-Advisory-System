package com.farmadvisory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDTO {
    private String _id;
    private String id;
    private String imageName;
    private String imagePath;
    private String analysis;
    private String filename;
    private String originalName;
    private String filepath;
    private String mimeType;
    private long size;
    private LocalDateTime createdAt;
}
