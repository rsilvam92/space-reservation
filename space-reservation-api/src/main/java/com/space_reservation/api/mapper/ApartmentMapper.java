package com.space_reservation.api.mapper;

import com.space_reservation.api.dto.request.ApartmentRequestDTO;
import com.space_reservation.api.dto.response.ApartmentResponseDTO;
import com.space_reservation.api.entity.Apartment;

public class ApartmentMapper {

    public static Apartment toEntity(ApartmentRequestDTO dto) {
        if (dto == null) return null;
        Apartment apartment = new Apartment();
        apartment.setSector(dto.getSector());
        apartment.setNumero(dto.getNumero());
        return apartment;
    }

    public static ApartmentResponseDTO toDTO(Apartment apartment) {
        if (apartment == null) return null;
        ApartmentResponseDTO dto = new ApartmentResponseDTO();
        dto.setId(apartment.getId());
        dto.setSector(apartment.getSector());
        dto.setNumero(apartment.getNumero());
        dto.setEstado(apartment.getEstado());
        return dto;
    }
}