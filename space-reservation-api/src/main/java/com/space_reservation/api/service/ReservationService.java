package com.space_reservation.api.service;

import com.space_reservation.api.entity.Reservation;

import java.util.List;

public interface ReservationService {

    Reservation createReservation(Reservation reservation);

    List<Reservation> getAll();

    List<Reservation> getByUser(Long userId);

    void cancelReservation(Long reservationId);
}