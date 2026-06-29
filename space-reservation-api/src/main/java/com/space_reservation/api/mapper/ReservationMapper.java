package com.space_reservation.api.mapper;

import com.space_reservation.api.dto.request.ReservationRequestDTO;
import com.space_reservation.api.dto.response.ReservationResponseDTO;
import com.space_reservation.api.entity.Reservation;

public class ReservationMapper {

    public static Reservation toEntity(ReservationRequestDTO dto) {
        if (dto == null) return null;
        Reservation reservation = new Reservation();
        reservation.setFecha(dto.getFecha());
        reservation.setHoraInicio(dto.getHoraInicio());
        reservation.setHoraFin(dto.getHoraFin());
        return reservation;
    }

    public static ReservationResponseDTO toDTO(Reservation reservation) {
        if (reservation == null) return null;
        ReservationResponseDTO dto = new ReservationResponseDTO();
        dto.setId(reservation.getId());
        dto.setFecha(reservation.getFecha());
        dto.setHoraInicio(reservation.getHoraInicio());
        dto.setHoraFin(reservation.getHoraFin());
        dto.setEstado(reservation.getEstado());
        dto.setFechaReserva(reservation.getFechaReserva());
        dto.setFechaConfirmacion(reservation.getFechaConfirmacion());

        if (reservation.getUser() != null) {
            dto.setUsuarioNombre(reservation.getUser().getNombre() + " " + reservation.getUser().getApellido());
        }

        if (reservation.getSpace() != null) {
            dto.setEspacioNombre(reservation.getSpace().getNombre());
        }
        return dto;
    }
}