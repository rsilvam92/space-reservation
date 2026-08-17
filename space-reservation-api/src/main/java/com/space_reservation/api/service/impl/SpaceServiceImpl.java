package com.space_reservation.api.service.impl;

import com.space_reservation.api.dto.request.SpaceRequestDTO;
import com.space_reservation.api.entity.Space;
import com.space_reservation.api.entity.SpaceConfiguration;
import com.space_reservation.api.entity.enums.SpaceType;
import com.space_reservation.api.exception.BusinessException;
import com.space_reservation.api.exception.ResourceNotFoundException;
import com.space_reservation.api.repository.CondominiumRepository;
import com.space_reservation.api.repository.SpaceRepository;
import com.space_reservation.api.service.SpaceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpaceServiceImpl implements SpaceService {

    private final SpaceRepository spaceRepository;
    private final CondominiumRepository condominiumRepository;

    @Override
    @Transactional
    public Space createSpace(SpaceRequestDTO dto) {

        String customType = resolveCustomType(dto);

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
        space.setActivo(dto.getActivo() == null || dto.getActivo());
        space.setCreatedAt(java.time.LocalDateTime.now());
        space.setUpdatedAt(java.time.LocalDateTime.now());
        applyConfiguration(space, dto);

        return spaceRepository.save(space);
    }

    @Override
    @Transactional
    public Space updateSpace(Long id, SpaceRequestDTO dto) {
        Space space = spaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El espacio con ID " + id + " no existe."
                ));

        if (spaceRepository.existsByNombreAndIdNot(dto.getNombre(), id)) {
            throw new BusinessException("Ya existe otro espacio con ese nombre");
        }

        com.space_reservation.api.entity.Condominium condominium =
                condominiumRepository.findById(dto.getCondominioId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "El condominio con ID " + dto.getCondominioId() + " no existe."
                        ));

        space.setNombre(dto.getNombre());
        space.setDescripcion(dto.getDescripcion());
        space.setTipo(dto.getTipo());
        space.setTipoPersonalizado(resolveCustomType(dto));
        space.setCondominium(condominium);
        if (dto.getActivo() != null) {
            space.setActivo(dto.getActivo());
        }
        space.setUpdatedAt(java.time.LocalDateTime.now());
        applyConfiguration(space, dto);

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

    private String resolveCustomType(SpaceRequestDTO dto) {
        if (dto.getTipo() != SpaceType.CUSTOM) {
            return null;
        }
        if (dto.getTipoPersonalizado() == null || dto.getTipoPersonalizado().isBlank()) {
            throw new BusinessException("Escribe el nombre del tipo de espacio personalizado.");
        }
        return dto.getTipoPersonalizado().trim();
    }

    private void applyConfiguration(Space space, SpaceRequestDTO dto) {
        SpaceConfiguration configuration = space.getConfiguracion();
        if (configuration == null) {
            configuration = new SpaceConfiguration();
            configuration.setSpace(space);
            space.setConfiguracion(configuration);
        }

        configuration.setMaxHorasReserva(dto.getMaxHorasReserva());
        configuration.setMaxReservasSemana(dto.getMaxReservasSemana());
        boolean requiresConfirmation = Boolean.TRUE.equals(dto.getRequiereConfirmacion());
        configuration.setRequiereConfirmacion(requiresConfirmation);
        configuration.setMinutosConfirmacion(
                requiresConfirmation ? dto.getMinutosConfirmacion() : null
        );
    }
}
