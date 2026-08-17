package com.space_reservation.api.dto.request;

import com.space_reservation.api.entity.enums.SpaceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
public class SpaceRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;

    @NotNull(message = "El tipo de espacio es obligatorio")
    private SpaceType tipo;

    @Size(max = 80, message = "El nombre del tipo personalizado no puede superar 80 caracteres")
    private String tipoPersonalizado;

    @NotNull(message = "Debe asociar el espacio a un condominio")
    private Long condominioId;

    private Integer maxHorasReserva;
    private Integer maxReservasSemana;
    private Boolean requiereConfirmacion;
    private Integer minutosConfirmacion;
    private Boolean activo;
}
