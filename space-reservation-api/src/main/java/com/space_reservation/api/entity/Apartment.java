package com.space_reservation.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "apartments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tower; // Ej: "Torre 2"

    @Column(nullable = false)
    private String number; // Ej: "201"

    @Column(nullable = false, unique = true)
    private String code; // Ej: "T2-201"
}