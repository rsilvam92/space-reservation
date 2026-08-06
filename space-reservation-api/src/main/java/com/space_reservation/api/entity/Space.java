package com.space_reservation.api.entity;

import com.space_reservation.api.entity.enums.SpaceType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "espacio")
@Getter
@Setter
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "condominio_id")
    private Condominium condominium;

    @Enumerated(EnumType.STRING)
    private SpaceType tipo;

    @Column(name = "tipo_personalizado", length = 80)
    private String tipoPersonalizado;

    private Boolean activo = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "space", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private SpaceConfiguration configuracion;
}
