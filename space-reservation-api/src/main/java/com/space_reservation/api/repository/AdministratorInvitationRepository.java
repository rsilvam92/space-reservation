package com.space_reservation.api.repository;

import com.space_reservation.api.entity.AdministratorInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministratorInvitationRepository extends JpaRepository<AdministratorInvitation, Long> {

    Optional<AdministratorInvitation> findByTokenHashAndUsedAtIsNull(String tokenHash);

    Optional<AdministratorInvitation> findFirstByCondominiumIdAndEmailIgnoreCaseAndUsedAtIsNullOrderByCreatedAtDesc(
            Long condominiumId,
            String email
    );
}
