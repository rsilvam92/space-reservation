package com.space_reservation.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AdministratorInvitationRequestDTO(
        @NotBlank(message = "El correo del administrador es obligatorio")
        @Email(message = "El correo del administrador no es válido")
        String email
) {
}
