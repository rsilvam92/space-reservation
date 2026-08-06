package com.space_reservation.api.repository;

import com.space_reservation.api.entity.Condominium;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CondominiumRepository
        extends JpaRepository<Condominium, Long> {

    boolean existsByNombreIgnoreCase(String nombre);
}
