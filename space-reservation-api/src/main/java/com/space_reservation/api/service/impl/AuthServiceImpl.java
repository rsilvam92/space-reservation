package com.space_reservation.api.service.impl;

import com.space_reservation.api.dto.request.LoginRequestDTO;
import com.space_reservation.api.dto.response.LoginResponseDTO;
import com.space_reservation.api.entity.User;
import com.space_reservation.api.repository.UserRepository;
import com.space_reservation.api.security.JwtService;
import com.space_reservation.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO request) {

        User user = userRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        if (user.getEstado() != null && "PENDING".equalsIgnoreCase(user.getEstado().toString().trim())) {
            throw new DisabledException("Tu usuario está pendiente de aprobación por el administrador.");
        }


        String token = jwtService.generateToken(user.getCorreo(), user.getRole().getNombre());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setId(user.getId());
        response.setCorreo(user.getCorreo());
        response.setToken(token);
        response.setRol(user.getRole().getNombre());
        response.setNombreCompleto(user.getNombre() + " " + user.getApellido());

        return response;
    }
}
