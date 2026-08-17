package com.space_reservation.api.dto.response;

public record RegistrationApartmentDTO(
        Long id,
        String sector,
        String numero,
        String estado
) {
}
