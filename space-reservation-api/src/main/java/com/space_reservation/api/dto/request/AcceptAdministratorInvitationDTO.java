package com.space_reservation.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AcceptAdministratorInvitationDTO(
        @NotBlank(message = "El token es obligatorio") String token,
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El apellido es obligatorio") String apellido,
        @NotBlank(message = "El documento es obligatorio")
        @Pattern(regexp = "^\\d+$", message = "El documento debe contener únicamente números")
        String documento,
        String telefono,
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres")
        String password
) {
}
