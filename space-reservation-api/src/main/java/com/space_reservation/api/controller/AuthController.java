package com.space_reservation.api.controller;

import com.space_reservation.api.dto.request.LoginRequestDTO;
import com.space_reservation.api.dto.request.AcceptAdministratorInvitationDTO;
import com.space_reservation.api.dto.response.LoginResponseDTO;
import com.space_reservation.api.dto.response.MessageResponseDTO;
import com.space_reservation.api.service.AuthService;
import com.space_reservation.api.service.PlatformOnboardingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PlatformOnboardingService platformOnboardingService;

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/administrator-invitations/accept")
    public MessageResponseDTO acceptAdministratorInvitation(
            @Valid @RequestBody AcceptAdministratorInvitationDTO request
    ) {
        return platformOnboardingService.acceptInvitation(request);
    }
}
