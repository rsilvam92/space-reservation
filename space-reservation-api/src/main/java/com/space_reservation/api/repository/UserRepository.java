package com.space_reservation.api.repository;

import com.space_reservation.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByCorreo(String correo);

    Optional<User> findByDocumento(String documento);

    boolean existsByCorreo(String correo);

    boolean existsByDocumento(String documento);

}