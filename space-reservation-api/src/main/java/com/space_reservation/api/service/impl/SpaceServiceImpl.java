package com.space_reservation.api.service.impl;

import com.space_reservation.api.entity.Space;
import com.space_reservation.api.entity.enums.SpaceType;
import com.space_reservation.api.repository.SpaceRepository;
import com.space_reservation.api.service.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceServiceImpl implements SpaceService {

    private final SpaceRepository spaceRepository;

    @Override
    public Space createSpace(Space space) {

        if (spaceRepository.existsByName(space.getName())) {
            throw new RuntimeException("El espacio ya existe");
        }

        return spaceRepository.save(space);
    }

    @Override
    public List<Space> getAll() {
        return spaceRepository.findAll();
    }

    @Override
    public List<Space> getByType(SpaceType type) {
        return spaceRepository.findByType(type);
    }
}