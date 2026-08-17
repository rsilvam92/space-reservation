package com.space_reservation.api.repository;

import com.space_reservation.api.entity.Condominium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CondominiumRepository
        extends JpaRepository<Condominium, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    List<Condominium> findByActivoTrueOrderByNombreAsc();
}
