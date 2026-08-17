package com.space_reservation.api.service.impl;

import com.space_reservation.api.dto.response.DashboardDTO;
import com.space_reservation.api.entity.enums.ReservationStatus;
import com.space_reservation.api.entity.enums.UserStatus;
import com.space_reservation.api.repository.ReservationRepository;
import com.space_reservation.api.repository.SpaceRepository;
import com.space_reservation.api.repository.UserRepository;
import com.space_reservation.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl
        implements DashboardService {

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    private final SpaceRepository spaceRepository;

    @Override
    public DashboardDTO getDashboard(Long condominiumId) {

        DashboardDTO dto = new DashboardDTO();

        if (condominiumId == null) {
            dto.setTotalUsuarios(userRepository.count());
            dto.setUsuariosPendientes(userRepository.countByEstado(UserStatus.PENDING));
            dto.setUsuariosActivos(userRepository.countByEstado(UserStatus.ACTIVE));
            dto.setTotalReservas(reservationRepository.count());
            dto.setReservasPendientes(
                    reservationRepository.countByEstado(ReservationStatus.PENDING)
            );
            dto.setReservasConfirmadas(
                    reservationRepository.countByEstado(ReservationStatus.CONFIRMED)
            );
            dto.setEspaciosDisponibles(spaceRepository.countByActivoTrue());
            return dto;
        }

        dto.setTotalUsuarios(userRepository.countByApartmentCondominiumId(condominiumId));
        dto.setUsuariosPendientes(
                userRepository.countByEstadoAndApartmentCondominiumId(
                        UserStatus.PENDING,
                        condominiumId
                )
        );
        dto.setUsuariosActivos(
                userRepository.countByEstadoAndApartmentCondominiumId(
                        UserStatus.ACTIVE,
                        condominiumId
                )
        );
        dto.setTotalReservas(reservationRepository.countBySpaceCondominiumId(condominiumId));
        dto.setReservasPendientes(
                reservationRepository.countByEstadoAndSpaceCondominiumId(
                        ReservationStatus.PENDING,
                        condominiumId
                )
        );
        dto.setReservasConfirmadas(
                reservationRepository.countByEstadoAndSpaceCondominiumId(
                        ReservationStatus.CONFIRMED,
                        condominiumId
                )
        );
        dto.setEspaciosDisponibles(
                spaceRepository.countByActivoTrueAndCondominiumId(condominiumId)
        );

        return dto;

    }

}
