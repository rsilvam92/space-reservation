package com.space_reservation.api.entity;

import com.space_reservation.api.entity.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "apartamento_id")
    private Apartment apartment;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Role role;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String documento;

    @Column(unique = true)
    private String correo;

    private String telefono;
    private String password;

    @Column(name = "tipo_ocupante")
    private String tipoOcupante;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20)
    private UserStatus estado;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}