package com.space_reservation.api.mapper;

import com.space_reservation.api.dto.UserRegisterDTO;
import com.space_reservation.api.dto.response.UserResponseDTO;
import com.space_reservation.api.entity.User;

public class UserMapper {

    public static User toEntity(UserRegisterDTO dto) {
        User user = new User();

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setDocumentNumber(dto.getDocumentNumber());
        user.setPassword(dto.getPassword());

        return user;
    }

    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setDocumentNumber(user.getDocumentNumber());
        dto.setRole(user.getRole());
        dto.setActive(user.isActive());

        if (user.getApartment() != null) {
            dto.setApartmentCode(user.getApartment().getCode());
        }

        return dto;
    }
}