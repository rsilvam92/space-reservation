package com.space_reservation.api.controller;

import com.space_reservation.api.dto.request.LoginRequestDTO;
import com.space_reservation.api.dto.response.LoginResponseDTO;
import com.space_reservation.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }
}