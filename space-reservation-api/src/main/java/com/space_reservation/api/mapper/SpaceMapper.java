package com.space_reservation.api.mapper;

import com.space_reservation.api.dto.request.SpaceRequestDTO;
import com.space_reservation.api.dto.response.SpaceResponseDTO;
import com.space_reservation.api.entity.Space;
import com.space_reservation.api.entity.SpaceConfiguration;

public class SpaceMapper {

    public static Space toEntity(SpaceRequestDTO dto) {
        if (dto == null) return null;

        Space space = new Space();
        space.setNombre(dto.getNombre());
        space.setTipo(dto.getTipo());
        space.setTipoPersonalizado(dto.getTipoPersonalizado());
        space.setDescripcion(dto.getDescripcion());
        space.setActivo(true);

        if (dto.getMaxHorasReserva() != null || dto.getMaxReservasSemana() != null) {
            SpaceConfiguration config = new SpaceConfiguration();
            config.setMaxHorasReserva(dto.getMaxHorasReserva());
            config.setMaxReservasSemana(dto.getMaxReservasSemana());

            config.setSpace(space);
            space.setConfiguracion(config);
        }

        return space;
    }

    public static SpaceResponseDTO toDTO(Space space) {
        if (space == null) return null;

        SpaceResponseDTO dto = new SpaceResponseDTO();
        dto.setId(space.getId());
        dto.setNombre(space.getNombre());
        dto.setTipo(space.getTipo());
        dto.setTipoPersonalizado(space.getTipoPersonalizado());
        dto.setDescripcion(space.getDescripcion());


        dto.setActivo(space.getActivo() != null ? space.getActivo() : false);

        if (space.getConfiguracion() != null) {
            dto.setMaxHorasReserva(space.getConfiguracion().getMaxHorasReserva());
            dto.setMaxReservasSemana(space.getConfiguracion().getMaxReservasSemana());
        }

        return dto;
    }
}
