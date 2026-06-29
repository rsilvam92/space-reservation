package com.space_reservation.api.service.impl;

import com.space_reservation.api.dto.request.ReservationRequestDTO;
import com.space_reservation.api.entity.Reservation;
import com.space_reservation.api.entity.enums.ReservationStatus;
import com.space_reservation.api.entity.enums.SpaceType;
import com.space_reservation.api.exception.BusinessException;
import com.space_reservation.api.exception.ResourceNotFoundException;
import com.space_reservation.api.repository.ReservationRepository;
import com.space_reservation.api.repository.SpaceRepository;
import com.space_reservation.api.repository.UserRepository;
import com.space_reservation.api.service.ReservationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final SpaceRepository spaceRepository;

    @Override
    public Reservation createReservation(ReservationRequestDTO dto) {
        com.space_reservation.api.entity.User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("El usuario con ID " + dto.getUserId() + " no existe."));

        com.space_reservation.api.entity.Space space = spaceRepository.findById(dto.getSpaceId())
                .orElseThrow(() -> new ResourceNotFoundException("El espacio con ID " + dto.getSpaceId() + " no existe."));

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setSpace(space);
        reservation.setFecha(dto.getFecha());
        reservation.setHoraInicio(dto.getHoraInicio());
        reservation.setHoraFin(dto.getHoraFin());

        validateTimeRange(reservation);
        validateOverlap(reservation);
        validateSpaceRules(reservation);

        reservation.setEstado(ReservationStatus.PENDING);
        reservation.setFechaReserva(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }

    @Override
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getByUser(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    @Override
    public void cancelReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        reservation.setEstado(ReservationStatus.CANCELLED);

        reservationRepository.save(reservation);

    }

    private void validateTimeRange(Reservation reservation) {

        SpaceType type = reservation.getSpace().getTipo();

        long hours = Duration.between(
                reservation.getHoraInicio(),
                reservation.getHoraFin()
        ).toHours();

        switch (type) {

            case COWORK -> {

                int startHour = reservation.getHoraInicio().getHour();
                int endHour = reservation.getHoraFin().getHour();

                if (startHour < 6) {
                    throw new BusinessException("Cowork inicia desde las 6 AM");
                }

                if (endHour > 23) {
                    throw new BusinessException("Cowork finaliza a las 11:59 PM");
                }

                if (hours > 5) {
                    throw new BusinessException("Máximo 5 horas");
                }
            }

            case SOCIAL_HALL -> {

                int endHour = reservation.getHoraFin().getHour();

                if (endHour > 1 && endHour < 6) {
                    throw new BusinessException("El salón social solo puede utilizarse hasta la 1 AM");
                }
            }

            case SAUNA -> {
            }

        }

    }

    private void validateOverlap(Reservation reservation) {

        List<Reservation> reservations =
                reservationRepository.findOverlappingReservations(
                        reservation.getSpace().getId(),
                        reservation.getFecha(),
                        reservation.getHoraInicio(),
                        reservation.getHoraFin()
                );

        if (!reservations.isEmpty()) {
            throw new BusinessException("Ya existe una reserva para ese horario.");
        }

    }

    private void validateSpaceRules(Reservation reservation) {

        if (reservation.getSpace().getTipo() == SpaceType.COWORK) {

            LocalDate inicioSemana = reservation.getFecha().with(DayOfWeek.MONDAY);

            LocalDate finSemana = inicioSemana.plusDays(6);

            Long total = reservationRepository.countWeeklyReservations(
                    reservation.getUser().getId(),
                    reservation.getSpace().getTipo(),
                    inicioSemana,
                    finSemana
            );

            if (total >= 2) {
                throw new BusinessException("Máximo dos reservas por semana.");
            }

        }

    }

    @Scheduled(fixedRate = 60000)
    public void releaseUnconfirmedReservations() {

        List<Reservation> reservations =
                reservationRepository.findByEstado(ReservationStatus.PENDING);

        for (Reservation reservation : reservations) {

            LocalDateTime startReservation = LocalDateTime.of(
                    reservation.getFecha(),
                    reservation.getHoraInicio()
            );

            LocalDateTime limit = startReservation.minusMinutes(30);

            if (LocalDateTime.now().isAfter(limit)) {

                reservation.setEstado(ReservationStatus.EXPIRED);

                reservationRepository.save(reservation);

            }
        }
    }

    @Override
    public Reservation confirmReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada"));

        reservation.setEstado(ReservationStatus.CONFIRMED);
        reservation.setFechaConfirmacion(LocalDateTime.now());

        return reservationRepository.save(reservation);
    }
}