package com.farmadvisory.service;

import com.farmadvisory.dto.CropDTO;
import com.farmadvisory.entity.Crop;
import com.farmadvisory.exception.BadRequestException;
import com.farmadvisory.exception.ResourceNotFoundException;
import com.farmadvisory.repository.CropRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CropService {

    private final CropRepository cropRepository;

    public List<CropDTO> getCrops(String userId) {
        return cropRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CropDTO createCrop(String userId, CropDTO request) {
        validateName(request.getName());

        Crop crop = Crop.builder()
                .userId(userId)
                .name(request.getName().trim())
                .variety(request.getVariety())
                .season(request.getSeason())
                .status(defaultText(request.getStatus(), "planned"))
                .area(request.getArea())
                .areaUnit(defaultText(request.getAreaUnit(), "acre"))
                .sowingDate(request.getSowingDate())
                .expectedHarvestDate(request.getExpectedHarvestDate())
                .notes(request.getNotes())
                .build();

        return mapToDTO(cropRepository.save(crop));
    }

    public CropDTO updateCrop(String userId, String cropId, CropDTO request) {
        Crop crop = cropRepository.findByIdAndUserId(cropId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found"));

        if (request.getName() != null) {
            validateName(request.getName());
            crop.setName(request.getName().trim());
        }
        if (request.getVariety() != null) {
            crop.setVariety(request.getVariety());
        }
        if (request.getSeason() != null) {
            crop.setSeason(request.getSeason());
        }
        if (request.getStatus() != null) {
            crop.setStatus(request.getStatus());
        }
        if (request.getArea() != null) {
            crop.setArea(request.getArea());
        }
        if (request.getAreaUnit() != null) {
            crop.setAreaUnit(request.getAreaUnit());
        }
        if (request.getSowingDate() != null) {
            crop.setSowingDate(request.getSowingDate());
        }
        if (request.getExpectedHarvestDate() != null) {
            crop.setExpectedHarvestDate(request.getExpectedHarvestDate());
        }
        if (request.getNotes() != null) {
            crop.setNotes(request.getNotes());
        }

        crop.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(cropRepository.save(crop));
    }

    public void deleteCrop(String userId, String cropId) {
        cropRepository.findByIdAndUserId(cropId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Crop not found"));
        cropRepository.deleteByIdAndUserId(cropId, userId);
    }

    private void validateName(String name) {
        if (name == null || name.trim().length() < 2) {
            throw new BadRequestException("Crop name must be at least 2 characters");
        }
    }

    private String defaultText(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value.trim();
    }

    private CropDTO mapToDTO(Crop crop) {
        return CropDTO.builder()
                ._id(crop.getId())
                .id(crop.getId())
                .name(crop.getName())
                .variety(crop.getVariety())
                .season(crop.getSeason())
                .status(crop.getStatus())
                .area(crop.getArea())
                .areaUnit(crop.getAreaUnit())
                .sowingDate(crop.getSowingDate())
                .expectedHarvestDate(crop.getExpectedHarvestDate())
                .notes(crop.getNotes())
                .createdAt(crop.getCreatedAt())
                .updatedAt(crop.getUpdatedAt())
                .build();
    }
}
