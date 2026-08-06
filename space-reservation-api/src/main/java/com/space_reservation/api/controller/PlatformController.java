package com.space_reservation.api.controller;

import com.space_reservation.api.dto.request.AdministratorInvitationRequestDTO;
import com.space_reservation.api.dto.request.CondominiumOnboardingRequestDTO;
import com.space_reservation.api.dto.response.AdministratorInvitationResponseDTO;
import com.space_reservation.api.dto.response.CondominiumOnboardingResponseDTO;
import com.space_reservation.api.service.PlatformOnboardingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/platform")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class PlatformController {

    private final PlatformOnboardingService onboardingService;

    @PostMapping("/condominiums")
    public CondominiumOnboardingResponseDTO onboardCondominium(
            @Valid @RequestBody CondominiumOnboardingRequestDTO request
    ) {
        return onboardingService.onboardCondominium(request);
    }

    @PostMapping("/condominiums/{condominiumId}/administrator-invitations")
    public AdministratorInvitationResponseDTO inviteAdministrator(
            @PathVariable Long condominiumId,
            @Valid @RequestBody AdministratorInvitationRequestDTO request
    ) {
        return onboardingService.inviteAdministrator(condominiumId, request);
    }
}
