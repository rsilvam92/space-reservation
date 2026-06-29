package com.space_reservation.api.service.impl;

import com.space_reservation.api.entity.Reservation;
import com.space_reservation.api.entity.enums.SpaceType;
import com.space_reservation.api.repository.ReservationRepository;
import com.space_reservation.api.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    public Reservation createReservation(Reservation reservation) {

        validateTimeRange(reservation);
        validateOverlap(reservation);
        validateSpaceRules(reservation);

        reservation.setConfirmed(false);

        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getByUser(Long userId) {
        return reservationRepository.findAll()
                .stream()
                .filter(r -> r.getUser().getId().equals(userId))
                .toList();
    }

    @Override
    public void cancelReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }

    private void validateTimeRange(Reservation r) {

        SpaceType type = r.getSpace().getType();

        long hours = java.time.Duration.between(
                r.getStartTime(),
                r.getEndTime()
        ).toHours();

        switch (type) {

            case COWORK -> {

                int startHour = r.getStartTime().getHour();
                int endHour = r.getEndTime().getHour();

                if (startHour < 6 || endHour > 0) {
                    throw new RuntimeException("Cowork: 6am a 12am");
                }

                if (hours > 5) {
                    throw new RuntimeException("Máximo 5 horas en cowork");
                }
            }

            case SOCIAL_HALL -> {

                int endHour = r.getEndTime().getHour();

                if (endHour > 1) {
                    throw new RuntimeException("Salones hasta 1am");
                }
            }

            case SAUNA -> {
                // libre
            }
        }
    }

    private void validateOverlap(Reservation r) {

        List<Reservation> existing =
                reservationRepository.findBySpace_Id(r.getSpace().getId());

        for (Reservation e : existing) {

            boolean overlap =
                    r.getStartTime().isBefore(e.getEndTime()) &&
                            r.getEndTime().isAfter(e.getStartTime());

            if (overlap) {
                throw new RuntimeException("El espacio ya está reservado en ese horario");
            }
        }
    }

    private void validateSpaceRules(Reservation r) {

        if (r.getSpace().getType() == SpaceType.COWORK) {

            LocalDateTime start = startOfWeek(r.getStartTime());
            LocalDateTime end = endOfWeek(r.getStartTime());

            Long weeklyCount = reservationRepository
                    .countUserReservationsInWeek(
                            r.getUser().getId(),
                            SpaceType.COWORK,
                            start,
                            end
                    );

            if (weeklyCount >= 2) {
                throw new RuntimeException("Máximo 2 reservas por semana en cowork");
            }
        }
    }

    public void confirmReservation(Long reservationId) {

        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("No existe reserva"));

        r.setConfirmed(true);
        reservationRepository.save(r);
    }

    @Scheduled(fixedRate = 60000)
    public void releaseUnconfirmedReservations() {

        List<Reservation> reservations = reservationRepository.findAll();

        for (Reservation r : reservations) {

            if (!r.isConfirmed()) {

                LocalDateTime limit = r.getStartTime().minusMinutes(30);

                if (LocalDateTime.now().isAfter(limit)) {
                    reservationRepository.delete(r);
                }
            }
        }
    }

    private LocalDateTime startOfWeek(LocalDateTime dateTime) {
        return dateTime
                .toLocalDate()
                .with(java.time.DayOfWeek.MONDAY)
                .atStartOfDay();
    }

    private LocalDateTime endOfWeek(LocalDateTime dateTime) {
        return dateTime
                .toLocalDate()
                .with(java.time.DayOfWeek.SUNDAY)
                .atTime(23, 59, 59);
    }
}