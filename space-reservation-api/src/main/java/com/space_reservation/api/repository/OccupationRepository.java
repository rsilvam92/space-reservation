package com.space_reservation.api.repository;

import com.space_reservation.api.entity.Occupation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OccupationRepository
        extends JpaRepository<Occupation, Long> {

    List<Occupation> findByApartmentId(Long apartmentId);

    List<Occupation> findByUserId(Long userId);

}