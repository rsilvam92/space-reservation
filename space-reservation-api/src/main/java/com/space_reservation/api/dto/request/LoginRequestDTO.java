package com.space_reservation.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    @Email(message = "Correo inválido")
    @NotBlank(message = "Correo obligatorio")
    private String correo;

    @NotBlank(message = "Contraseña obligatoria")
    private String password;
}