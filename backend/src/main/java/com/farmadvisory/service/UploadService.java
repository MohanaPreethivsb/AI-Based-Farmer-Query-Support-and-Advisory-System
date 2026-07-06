package com.farmadvisory.service;

import com.farmadvisory.dto.ImageDTO;
import com.farmadvisory.entity.Image;
import com.farmadvisory.exception.BadRequestException;
import com.farmadvisory.repository.ImageRepository;
import com.farmadvisory.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final ImageRepository imageRepository;

    public ImageDTO uploadImage(String userId, MultipartFile file) throws IOException {
        // Validate file
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (!FileUploadUtil.isValidImageFile(file)) {
            throw new BadRequestException("Invalid image file. Please upload JPG, PNG, GIF, or WebP");
        }

        // Save file
        String filename = FileUploadUtil.saveFile(file);

        // Save metadata to database
        Image image = Image.builder()
                .userId(userId)
                .filename(filename)
                .originalName(file.getOriginalFilename())
                .filepath("/uploads/" + filename)
                .mimeType(file.getContentType())
                .size(file.getSize())
                .build();

        image = imageRepository.save(image);

        return mapToDTO(image);
    }

    public List<ImageDTO> getUploadedImages(String userId) {
        List<Image> images = imageRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return images.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ImageDTO mapToDTO(Image image) {
        return ImageDTO.builder()
                ._id(image.getId())
                .id(image.getId())
                .imageName(image.getOriginalName())
                .imagePath(image.getFilepath())
                .analysis("Image uploaded successfully. AI image diagnosis can be added to this saved crop photo.")
                .filename(image.getFilename())
                .originalName(image.getOriginalName())
                .filepath(image.getFilepath())
                .mimeType(image.getMimeType())
                .size(image.getSize())
                .createdAt(image.getCreatedAt())
                .build();
    }
}
