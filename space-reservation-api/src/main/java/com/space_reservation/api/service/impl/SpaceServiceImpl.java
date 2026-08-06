package com.space_reservation.api.service.impl;

import com.space_reservation.api.dto.request.SpaceRequestDTO;
import com.space_reservation.api.entity.Space;
import com.space_reservation.api.entity.enums.SpaceType;
import com.space_reservation.api.exception.BusinessException;
import com.space_reservation.api.exception.ResourceNotFoundException;
import com.space_reservation.api.repository.CondominiumRepository;
import com.space_reservation.api.repository.SpaceRepository;
import com.space_reservation.api.service.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceServiceImpl implements SpaceService {

    private final SpaceRepository spaceRepository;
    private final CondominiumRepository condominiumRepository;

    @Override
    public Space createSpace(SpaceRequestDTO dto) {

        String customType = null;
        if (dto.getTipo() == SpaceType.CUSTOM) {
            if (dto.getTipoPersonalizado() == null || dto.getTipoPersonalizado().isBlank()) {
                throw new BusinessException("Escribe el nombre del tipo de espacio personalizado.");
            }
            customType = dto.getTipoPersonalizado().trim();
        }

        if (spaceRepository.existsByNombre(dto.getNombre())) {
            throw new BusinessException("Ya existe un espacio con ese nombre");
        }

        com.space_reservation.api.entity.Condominium condominium = condominiumRepository.findById(dto.getCondominioId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El condominio con ID " + dto.getCondominioId() + " no existe."
                ));


        Space space = new Space();
        space.setNombre(dto.getNombre());
        space.setDescripcion(dto.getDescripcion());
        space.setTipo(dto.getTipo());
        space.setTipoPersonalizado(customType);
        space.setCondominium(condominium);
        space.setActivo(true);
        space.setCreatedAt(java.time.LocalDateTime.now());
        space.setUpdatedAt(java.time.LocalDateTime.now());

        return spaceRepository.save(space);
    }

    @Override
    public List<Space> getAll() {
        return spaceRepository.findAll();
    }

    @Override
    public List<Space> getByType(SpaceType type) {
        return spaceRepository.findByTipo(type);
    }
}
