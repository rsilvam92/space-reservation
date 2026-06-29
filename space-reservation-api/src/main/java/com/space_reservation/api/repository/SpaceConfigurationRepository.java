package com.space_reservation.api.repository;

import com.space_reservation.api.entity.SpaceConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpaceConfigurationRepository
        extends JpaRepository<SpaceConfiguration, Long> {

    Optional<SpaceConfiguration> findBySpaceId(Long spaceId);

}