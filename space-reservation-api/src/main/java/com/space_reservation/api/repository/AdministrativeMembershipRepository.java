package com.space_reservation.api.repository;

import com.space_reservation.api.entity.AdministrativeMembership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministrativeMembershipRepository extends JpaRepository<AdministrativeMembership, Long> {
}
