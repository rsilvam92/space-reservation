package com.space_reservation.api.repository;

import com.space_reservation.api.entity.SpaceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceScheduleRepository
        extends JpaRepository<SpaceSchedule, Long> {

    List<SpaceSchedule> findBySpaceId(Long spaceId);

}