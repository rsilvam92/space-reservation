package com.space_reservation.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "horario_espacio")
@Getter
@Setter
public class SpaceSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "espacio_id")
    private Space space;

    @Column(name = "dia_semana")
    private String diaSemana;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    private Boolean activo = true;
}