package com.space_reservation.api.controller;

import com.space_reservation.api.entity.Apartment;
import com.space_reservation.api.service.ApartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
@RequiredArgsConstructor
public class ApartmentController {

    private final ApartmentService apartmentService;

    @PostMapping
    public Apartment create(@Valid @RequestBody Apartment apartment) {
        return apartmentService.createApartment(apartment);
    }

    @GetMapping
    public List<Apartment> getAll() {
        return apartmentService.getAll();
    }
}