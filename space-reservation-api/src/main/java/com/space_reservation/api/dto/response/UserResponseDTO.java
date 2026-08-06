package com.space_reservation.api.dto.response;

import lombok.*;

@Getter
@Setter
public class UserResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String documento;
    private String rol;
    private String estado;
    private Long apartamentoId;
    private String apartamento;
}