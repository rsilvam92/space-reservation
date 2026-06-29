package com.space_reservation.api.service;

import com.space_reservation.api.entity.Apartment;

import java.util.List;

public interface ApartmentService {

    Apartment createApartment(Apartment apartment);

    List<Apartment> getAll();
}