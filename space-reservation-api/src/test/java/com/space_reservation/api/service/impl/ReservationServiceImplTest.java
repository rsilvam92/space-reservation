package com.space_reservation.api.service.impl;

import com.space_reservation.api.dto.request.ReservationRequestDTO;
import com.space_reservation.api.entity.Reservation;
import com.space_reservation.api.entity.Space;
import com.space_reservation.api.entity.User;
import com.space_reservation.api.entity.enums.ReservationStatus;
import com.space_reservation.api.entity.enums.SpaceType;
import com.space_reservation.api.repository.ReservationRepository;
import com.space_reservation.api.repository.SpaceRepository;
import com.space_reservation.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservationServiceImplTest {

    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private SpaceRepository spaceRepository;
    private ReservationServiceImpl service;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        userRepository = mock(UserRepository.class);
        spaceRepository = mock(SpaceRepository.class);
        service = new ReservationServiceImpl(reservationRepository, userRepository, spaceRepository);
    }

    @Test
    void createsReservationForPadelCourt() {
        User user = new User();
        user.setId(20L);

        Space space = new Space();
        space.setId(30L);
        space.setTipo(SpaceType.PADEL_COURT);

        ReservationRequestDTO request = new ReservationRequestDTO();
        request.setUserId(20L);
        request.setSpaceId(30L);
        request.setFecha(LocalDate.of(2026, 8, 10));
        request.setHoraInicio(LocalTime.of(10, 0));
        request.setHoraFin(LocalTime.of(11, 0));

        when(userRepository.findById(20L)).thenReturn(Optional.of(user));
        when(spaceRepository.findById(30L)).thenReturn(Optional.of(space));
        when(reservationRepository.findOverlappingReservations(
                30L, request.getFecha(), request.getHoraInicio(), request.getHoraFin()
        )).thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Reservation created = service.createReservation(request);

        assertSame(user, created.getUser());
        assertSame(space, created.getSpace());
        assertEquals(ReservationStatus.PENDING, created.getEstado());
        assertNotNull(created.getFechaReserva());
        verify(reservationRepository).save(created);
        verify(reservationRepository, never()).countWeeklyReservations(any(), any(), any(), any());
    }
}
