package com.space_reservation.api.security;

import com.space_reservation.api.entity.User;
import com.space_reservation.api.exception.BusinessException;
import com.space_reservation.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public User requireUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("No existe una sesión autenticada.");
        }
        return userRepository.findByCorreoIgnoreCase(authentication.getName())
                .orElseThrow(() -> new BusinessException("El usuario autenticado ya no existe."));
    }
}
