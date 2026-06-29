package com.space_reservation.api.repository;

import com.space_reservation.api.entity.Space;
import com.space_reservation.api.entity.enums.SpaceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpaceRepository
        extends JpaRepository<Space, Long> {

    List<Space> findByCondominiumId(Long condominiumId);

    List<Space> findByTipo(SpaceType tipo);

    List<Space> findByActivoTrue();

    boolean existsByNombre(String nombre);

}