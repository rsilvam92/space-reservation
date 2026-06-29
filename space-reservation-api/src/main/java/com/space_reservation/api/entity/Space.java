package com.space_reservation.api.entity;

import com.space_reservation.api.entity.enums.SpaceType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "spaces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Cowork, Sauna, Salon 1

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpaceType type;

    // reglas básicas (pueden ser null si no aplica)
    private Integer maxHours;

    private Integer maxWeeklyReservations;
}