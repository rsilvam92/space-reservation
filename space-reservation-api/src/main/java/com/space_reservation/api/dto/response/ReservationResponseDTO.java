package com.space_reservation.api.dto.response;

import com.space_reservation.api.entity.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationResponseDTO {
    private Long id;
    private Long usuarioId;
    private String usuarioNombre;
    private Long espacioId;
    private String espacioNombre;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private ReservationStatus estado;
    private LocalDateTime fechaReserva;
    private LocalDateTime fechaConfirmacion;
}