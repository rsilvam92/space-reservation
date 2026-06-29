package com.space_reservation.api.controller;

import com.space_reservation.api.entity.Reservation;
import com.space_reservation.api.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public Reservation create(@RequestBody Reservation reservation) {
        return reservationService.createReservation(reservation);
    }

    @GetMapping
    public List<Reservation> getAll() {
        return reservationService.getAll();
    }

    @GetMapping("/user/{userId}")
    public List<Reservation> getByUser(@PathVariable Long userId) {
        return reservationService.getByUser(userId);
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable Long id) {
        reservationService.cancelReservation(id);
    }
}