package com.space_reservation.api.dto.response;

import com.space_reservation.api.entity.Condominium;

public record CondominiumOnboardingResponseDTO(
        Condominium condominium,
        AdministratorInvitationResponseDTO invitation
) {
}
