package com.space_reservation.api.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PendingUserDTO {
    private Long id;
    private String nombreCompleto;
    private String correo;
    private String apartamento;
    private Long condominiumId;
    private LocalDateTime fechaRegistro;
}
