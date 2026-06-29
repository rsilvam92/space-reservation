package com.space_reservation.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "configuracion_espacio")
@Getter
@Setter
public class SpaceConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "espacio_id")
    private Space space;

    @Column(name = "max_horas_reserva")
    private Integer maxHorasReserva;
    @Column(name = "max_reservas_semana")
    private Integer maxReservasSemana;
    @Column(name = "requiere_confirmacion")
    private Boolean requiereConfirmacion;
    @Column(name = "minutos_confirmacion")
    private Integer minutosConfirmacion;
}