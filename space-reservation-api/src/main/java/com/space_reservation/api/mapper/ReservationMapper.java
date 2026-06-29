package com.space_reservation.api.mapper;

import com.space_reservation.api.dto.request.ReservationRequestDTO;
import com.space_reservation.api.dto.response.ReservationResponseDTO;
import com.space_reservation.api.entity.Reservation;

public class ReservationMapper {

    public static Reservation toEntity(ReservationRequestDTO dto) {
        Reservation reservation = new Reservation();

        reservation.setStartTime(dto.getStartTime());
        reservation.setEndTime(dto.getEndTime());

        return reservation;
    }

    public static ReservationResponseDTO toDTO(Reservation reservation) {
        ReservationResponseDTO dto = new ReservationResponseDTO();

        dto.setId(reservation.getId());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setStatus(reservation.getStatus());
        dto.setConfirmed(reservation.isConfirmed());

        if (reservation.getUser() != null) {
            dto.setUserName(reservation.getUser().getFullName());
        }

        if (reservation.getSpace() != null) {
            dto.setSpaceName(reservation.getSpace().getName());
        }

        return dto;
    }
}