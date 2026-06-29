package com.space_reservation.api.repository;

import com.space_reservation.api.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    Optional<Apartment> findByCode(String code);

    boolean existsByCode(String code);
}