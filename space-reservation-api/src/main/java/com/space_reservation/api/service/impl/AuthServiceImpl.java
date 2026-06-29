package com.space_reservation.api.service.impl;

import com.space_reservation.api.dto.request.LoginRequestDTO;
import com.space_reservation.api.dto.response.LoginResponseDTO;
import com.space_reservation.api.entity.User;
import com.space_reservation.api.repository.UserRepository;
import com.space_reservation.api.security.JwtService;
import com.space_reservation.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);

        return response;
    }
}