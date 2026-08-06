package com.space_reservation.api.service;

import com.space_reservation.api.dto.request.AcceptAdministratorInvitationDTO;
import com.space_reservation.api.dto.request.AdministratorInvitationRequestDTO;
import com.space_reservation.api.dto.request.CondominiumOnboardingRequestDTO;
import com.space_reservation.api.dto.response.AdministratorInvitationResponseDTO;
import com.space_reservation.api.dto.response.CondominiumOnboardingResponseDTO;
import com.space_reservation.api.dto.response.MessageResponseDTO;

public interface PlatformOnboardingService {

    CondominiumOnboardingResponseDTO onboardCondominium(CondominiumOnboardingRequestDTO request);

    AdministratorInvitationResponseDTO inviteAdministrator(
            Long condominiumId,
            AdministratorInvitationRequestDTO request
    );

    MessageResponseDTO acceptInvitation(AcceptAdministratorInvitationDTO request);
}
