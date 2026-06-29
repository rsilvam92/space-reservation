package com.space_reservation.api.repository;

import com.space_reservation.api.entity.Reservation;
import com.space_reservation.api.entity.enums.SpaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findBySpace_Id(Long spaceId);

    @Query("""
        SELECT COUNT(r)
        FROM Reservation r
        WHERE r.user.id = :userId
        AND r.space.type = :type
        AND r.startTime BETWEEN :start AND :end
    """)
    Long countUserReservationsInWeek(
            Long userId,
            SpaceType type,
            LocalDateTime start,
            LocalDateTime end
    );
}