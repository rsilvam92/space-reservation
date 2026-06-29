package com.space_reservation.api.dto.response;

import com.space_reservation.api.entity.enums.UserRole;
import lombok.*;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String fullName;
    private String email;
    private String documentNumber;
    private UserRole role;
    private boolean active;
    private String apartmentCode;
}