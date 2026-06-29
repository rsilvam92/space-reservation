package com.space_reservation.api.service.impl;

import com.space_reservation.api.entity.Apartment;
import com.space_reservation.api.repository.ApartmentRepository;
import com.space_reservation.api.service.ApartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;

    @Override
    public Apartment createApartment(Apartment apartment) {

        if (apartmentRepository.existsByCode(apartment.getCode())) {
            throw new RuntimeException("El apartamento ya existe");
        }

        return apartmentRepository.save(apartment);
    }

    @Override
    public List<Apartment> getAll() {
        return apartmentRepository.findAll();
    }
}