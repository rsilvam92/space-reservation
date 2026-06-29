package com.space_reservation.api.service;

import com.space_reservation.api.dto.request.LoginRequestDTO;
import com.space_reservation.api.dto.response.LoginResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO request);
}