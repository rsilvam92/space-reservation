package com.space_reservation.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CondominiumInputDTO(
        @NotBlank(message = "El nombre del condominio es obligatorio") String nombre,
        @NotBlank(message = "La dirección es obligatoria") String direccion,
        @NotBlank(message = "La ciudad es obligatoria") String ciudad,
        String tipoSector
) {
}
