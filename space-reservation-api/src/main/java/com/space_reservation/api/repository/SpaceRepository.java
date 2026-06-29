package com.space_reservation.api.repository;

import com.space_reservation.api.entity.Space;
import com.space_reservation.api.entity.enums.SpaceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {

    List<Space> findByType(SpaceType type);

    boolean existsByName(String name);
}