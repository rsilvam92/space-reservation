package com.space_reservation.api.mapper;

import com.space_reservation.api.dto.ApartmentDTO;
import com.space_reservation.api.dto.response.ApartmentResponseDTO;
import com.space_reservation.api.entity.Apartment;

public class ApartmentMapper {

    public static Apartment toEntity(ApartmentDTO dto) {
        Apartment apartment = new Apartment();

        apartment.setTower(dto.getTower());
        apartment.setNumber(dto.getNumber());

        return apartment;
    }

    public static ApartmentResponseDTO toDTO(Apartment apartment) {
        ApartmentResponseDTO dto = new ApartmentResponseDTO();

        dto.setId(apartment.getId());
        dto.setCode(apartment.getCode());

        return dto;
    }
}