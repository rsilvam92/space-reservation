package com.space_reservation.api.repository;

import com.space_reservation.api.entity.User;
import com.space_reservation.api.entity.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByCorreo(String correo);

    Optional<User> findByCorreoIgnoreCase(String correo);

    Optional<User> findByDocumento(String documento);

    boolean existsByCorreo(String correo);

    boolean existsByCorreoIgnoreCase(String correo);

    boolean existsByDocumento(String documento);

    List<User> findByEstado(UserStatus estado);

    Long countByEstado(UserStatus estado);

    Long countByApartmentCondominiumId(Long condominiumId);

    Long countByEstadoAndApartmentCondominiumId(UserStatus estado, Long condominiumId);

}
