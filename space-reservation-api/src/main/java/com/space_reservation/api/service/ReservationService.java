package com.space_reservation.api.service;

import com.space_reservation.api.dto.request.ReservationRequestDTO;
import com.space_reservation.api.entity.Reservation;

import java.util.List;

public interface ReservationService {

    Reservation createReservation(ReservationRequestDTO reservation);

    List<Reservation> getAll();

    List<Reservation> getByUser(Long userId);

    void cancelReservation(Long reservationId);

    Reservation confirmReservation(Long reservationId);
}