package com.space_reservation.api.dto.response;

import com.space_reservation.api.entity.Role;
import lombok.*;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String documento;
    private String rolNombre;
    private String estado;
    private Long apartamentoId;
    private String apartamentoDetalle;
}