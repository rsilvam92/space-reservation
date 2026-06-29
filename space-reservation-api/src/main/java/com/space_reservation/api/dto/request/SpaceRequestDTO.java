package com.space_reservation.api.dto.request;

import com.space_reservation.api.entity.enums.SpaceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class SpaceRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El tipo de espacio es obligatorio")
    private SpaceType tipo;

    @NotNull(message = "Debe asociar el espacio a un condominio")
    private Long condominioId;

    private Integer maxHorasReserva;
    private Integer maxReservasSemana;
}