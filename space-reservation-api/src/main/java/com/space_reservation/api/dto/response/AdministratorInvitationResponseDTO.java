package com.space_reservation.api.dto.response;

import java.time.LocalDateTime;

public record AdministratorInvitationResponseDTO(
        Long invitationId,
        Long condominiumId,
        String condominiumName,
        String administratorEmail,
        LocalDateTime expiresAt,
        String activationLink
) {
}
