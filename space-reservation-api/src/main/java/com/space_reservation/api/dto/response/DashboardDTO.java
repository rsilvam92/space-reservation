package com.space_reservation.api.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardDTO {
    private Long totalUsuarios;
    private Long usuariosPendientes;
    private Long usuariosActivos;
    private Long totalReservas;
    private Long reservasPendientes;
    private Long reservasConfirmadas;
    private Long espaciosDisponibles;
}