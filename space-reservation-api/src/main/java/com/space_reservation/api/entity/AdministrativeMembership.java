package com.space_reservation.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "administrador_condominio",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_administrador_condominio",
                columnNames = {"usuario_id", "condominio_id"}
        )
)
@Getter
@Setter
public class AdministrativeMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "condominio_id", nullable = false)
    private Condominium condominium;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
