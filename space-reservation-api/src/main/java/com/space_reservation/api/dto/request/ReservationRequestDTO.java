package com.space_reservation.api.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.*;


@Getter
@Setter
public class ReservationRequestDTO {

    @NotNull
    private Long userId;

    @NotNull
    private Long spaceId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @NotNull
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaInicio;

    @NotNull
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaFin;

}