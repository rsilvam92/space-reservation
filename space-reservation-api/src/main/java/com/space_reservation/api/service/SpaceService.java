package com.space_reservation.api.service;

import com.space_reservation.api.entity.Space;
import com.space_reservation.api.entity.enums.SpaceType;

import java.util.List;

public interface SpaceService {

    Space createSpace(Space space);

    List<Space> getAll();

    List<Space> getByType(SpaceType type);
}