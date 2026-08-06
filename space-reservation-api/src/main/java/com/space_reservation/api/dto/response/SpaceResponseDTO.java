package com.space_reservation.api.dto.response;

import com.space_reservation.api.entity.enums.SpaceType;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
public class SpaceResponseDTO {
    private Long id;
    private String nombre;
    private SpaceType tipo;
    private String tipoPersonalizado;
    private String descripcion;
    private boolean activo;
    private Integer maxHorasReserva;
    private Integer maxReservasSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
