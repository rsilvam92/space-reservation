package com.space_reservation.api.dto.response;

import lombok.*;

@Getter
@Setter
public class ApartmentResponseDTO {
    private Long id;
    private String sector;
    private String numero;
    private String estado;
    private String codigo;
}