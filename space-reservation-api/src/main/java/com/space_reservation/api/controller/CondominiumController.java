package com.space_reservation.api.controller;

import com.space_reservation.api.entity.Condominium;
import com.space_reservation.api.repository.CondominiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/condominiums")
@RequiredArgsConstructor
public class CondominiumController {

    private final CondominiumRepository condominiumRepository;

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public List<Condominium> findAll() {
        return condominiumRepository.findAll();
    }
}
