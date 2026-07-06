package com.farmadvisory.util;

import com.farmadvisory.dto.UserDTO;
import com.farmadvisory.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .district(user.getDistrict())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
