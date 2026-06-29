package com.space_reservation.api.entity;

import com.space_reservation.api.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reserva")
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "espacio_id")
    private Space space;

    private LocalDate fecha;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @Enumerated(EnumType.STRING)
    private ReservationStatus estado;

    @Column(name = "fecha_reserva")
    private LocalDateTime fechaReserva;

    @Column(name = "fecha_confirmacion")
    private LocalDateTime fechaConfirmacion;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}