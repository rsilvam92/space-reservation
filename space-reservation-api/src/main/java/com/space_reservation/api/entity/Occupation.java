package com.space_reservation.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ocupacion")
@Getter
@Setter
public class Occupation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "apartamento_id")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    private Boolean activa = true;
}