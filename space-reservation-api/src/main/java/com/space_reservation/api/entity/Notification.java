package com.space_reservation.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    private String tipo;

    private Boolean leida = false;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;
}