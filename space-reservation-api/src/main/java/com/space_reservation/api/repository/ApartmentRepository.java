package com.space_reservation.api.repository;

import com.space_reservation.api.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApartmentRepository
        extends JpaRepository<Apartment, Long> {

    List<Apartment> findByCondominiumId(Long condominiumId);

    List<Apartment> findByCondominiumIdOrderBySectorAscNumeroAsc(Long condominiumId);

    Optional<Apartment> findByCondominiumIdAndNumero(
            Long condominiumId,
            String numero
    );

}
