package com.space_reservation.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "condominio")
@Getter
@Setter
public class Condominium {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String direccion;
    private String ciudad;

    @Column(name = "tipo_sector")
    private String tipoSector;

    private Boolean activo = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}