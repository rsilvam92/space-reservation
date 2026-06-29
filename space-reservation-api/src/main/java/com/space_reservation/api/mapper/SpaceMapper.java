package com.space_reservation.api.mapper;

import com.space_reservation.api.dto.SpaceDTO;
import com.space_reservation.api.dto.response.SpaceResponseDTO;
import com.space_reservation.api.entity.Space;

public class SpaceMapper {

    public static Space toEntity(SpaceDTO dto) {
        Space space = new Space();

        space.setName(dto.getName());
        space.setType(dto.getType());
        space.setMaxHours(dto.getMaxHours());
        space.setMaxWeeklyReservations(dto.getMaxWeeklyReservations());

        return space;
    }

    public static SpaceResponseDTO toDTO(Space space) {
        SpaceResponseDTO dto = new SpaceResponseDTO();

        dto.setId(space.getId());
        dto.setName(space.getName());
        dto.setType(space.getType());

        return dto;
    }
}