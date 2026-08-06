package com.space_reservation.api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CondominiumOnboardingRequestDTO(
        @NotNull(message = "Los datos del condominio son obligatorios")
        @Valid
        CondominiumInputDTO condominium,

        @NotBlank(message = "El correo del administrador es obligatorio")
        @Email(message = "El correo del administrador no es válido")
        String administratorEmail
) {
}
