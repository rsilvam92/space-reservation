package com.space_reservation.api.service.impl;

import com.space_reservation.api.dto.response.DashboardDTO;
import com.space_reservation.api.entity.enums.ReservationStatus;
import com.space_reservation.api.entity.enums.UserStatus;
import com.space_reservation.api.repository.ReservationRepository;
import com.space_reservation.api.repository.SpaceRepository;
import com.space_reservation.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DashboardServiceImplTest {

    private UserRepository userRepository;
    private ReservationRepository reservationRepository;
    private SpaceRepository spaceRepository;
    private DashboardServiceImpl service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        spaceRepository = mock(SpaceRepository.class);
        service = new DashboardServiceImpl(
                userRepository,
                reservationRepository,
                spaceRepository
        );
    }

    @Test
    void getDashboardFiltersEveryMetricByCondominium() {
        long condominiumId = 2L;
        when(userRepository.countByApartmentCondominiumId(condominiumId)).thenReturn(10L);
        when(userRepository.countByEstadoAndApartmentCondominiumId(
                UserStatus.PENDING,
                condominiumId
        )).thenReturn(1L);
        when(userRepository.countByEstadoAndApartmentCondominiumId(
                UserStatus.ACTIVE,
                condominiumId
        )).thenReturn(9L);
        when(reservationRepository.countBySpaceCondominiumId(condominiumId)).thenReturn(7L);
        when(reservationRepository.countByEstadoAndSpaceCondominiumId(
                ReservationStatus.PENDING,
                condominiumId
        )).thenReturn(2L);
        when(reservationRepository.countByEstadoAndSpaceCondominiumId(
                ReservationStatus.CONFIRMED,
                condominiumId
        )).thenReturn(5L);
        when(spaceRepository.countByActivoTrueAndCondominiumId(condominiumId)).thenReturn(8L);

        DashboardDTO result = service.getDashboard(condominiumId);

        assertEquals(10L, result.getTotalUsuarios());
        assertEquals(1L, result.getUsuariosPendientes());
        assertEquals(9L, result.getUsuariosActivos());
        assertEquals(7L, result.getTotalReservas());
        assertEquals(2L, result.getReservasPendientes());
        assertEquals(5L, result.getReservasConfirmadas());
        assertEquals(8L, result.getEspaciosDisponibles());
        verify(userRepository).countByApartmentCondominiumId(condominiumId);
        verify(reservationRepository).countBySpaceCondominiumId(condominiumId);
        verify(spaceRepository).countByActivoTrueAndCondominiumId(condominiumId);
    }
}
