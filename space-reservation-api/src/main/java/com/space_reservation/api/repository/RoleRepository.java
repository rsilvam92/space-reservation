package com.space_reservation.api.repository;

import com.space_reservation.api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNombre(String nombre);

}