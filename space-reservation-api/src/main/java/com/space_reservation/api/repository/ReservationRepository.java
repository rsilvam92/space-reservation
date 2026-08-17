package com.space_reservation.api.repository;

import com.space_reservation.api.entity.Reservation;
import com.space_reservation.api.entity.enums.ReservationStatus;
import com.space_reservation.api.entity.enums.SpaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByEstado(ReservationStatus reservationStatus);

    List<Reservation> findBySpaceId(Long spaceId);

    List<Reservation> findByFecha(LocalDate fecha);

    List<Reservation> findBySpaceIdAndFecha(
            Long spaceId,
            LocalDate fecha
    );

    @Query("SELECT r FROM Reservation r WHERE r.space.id = :spaceId " +
            "AND r.fecha = :fecha " +
            "AND r.estado IN (com.space_reservation.api.entity.enums.ReservationStatus.PENDING, com.space_reservation.api.entity.enums.ReservationStatus.CONFIRMED) " +
            "AND ((r.horaInicio < :horaFin AND r.horaFin > :horaInicio))")
    List<Reservation> findOverlappingReservations(
            @Param("spaceId") Long spaceId,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin
    );

    @Query("""
        SELECT COUNT(r)
        FROM Reservation r
        WHERE r.user.id = :userId
        AND r.space.tipo = :tipo
        AND r.fecha BETWEEN :inicioSemana AND :finSemana
    """)
    Long countWeeklyReservations(
            Long userId,
            SpaceType tipo,
            LocalDate inicioSemana,
            LocalDate finSemana
    );

    Long countByEstado(ReservationStatus estado);

    Long countBySpaceCondominiumId(Long condominiumId);

    Long countByEstadoAndSpaceCondominiumId(
            ReservationStatus estado,
            Long condominiumId
    );
}
