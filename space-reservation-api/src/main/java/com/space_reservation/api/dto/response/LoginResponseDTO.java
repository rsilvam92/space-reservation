package com.space_reservation.api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private String rol;
    private String nombreCompleto;
}