package com.space_reservation.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "apartamento")
@Getter
@Setter
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "condominio_id")
    private Condominium condominium;

    private String sector;
    private String numero;
    private String estado;
}