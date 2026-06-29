package com.space_reservation.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol")
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
}