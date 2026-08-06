package com.space_reservation.api.service.impl;

import com.space_reservation.api.dto.request.SpaceRequestDTO;
import com.space_reservation.api.entity.Condominium;
import com.space_reservation.api.entity.Space;
import com.space_reservation.api.entity.enums.SpaceType;
import com.space_reservation.api.repository.CondominiumRepository;
import com.space_reservation.api.repository.SpaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SpaceServiceImplTest {

    private SpaceRepository spaceRepository;
    private CondominiumRepository condominiumRepository;
    private SpaceServiceImpl service;

    @BeforeEach
    void setUp() {
        spaceRepository = mock(SpaceRepository.class);
        condominiumRepository = mock(CondominiumRepository.class);
        service = new SpaceServiceImpl(spaceRepository, condominiumRepository);
    }

    @Test
    void createsTurkishBathSpace() {
        Condominium condominium = new Condominium();
        condominium.setId(10L);

        SpaceRequestDTO request = new SpaceRequestDTO();
        request.setNombre("Baño turco");
        request.setDescripcion("Zona húmeda");
        request.setTipo(SpaceType.TURKISH_BATH);
        request.setCondominioId(10L);

        when(spaceRepository.existsByNombre("Baño turco")).thenReturn(false);
        when(condominiumRepository.findById(10L)).thenReturn(Optional.of(condominium));
        when(spaceRepository.save(any(Space.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Space created = service.createSpace(request);

        assertEquals(SpaceType.TURKISH_BATH, created.getTipo());
        assertSame(condominium, created.getCondominium());
        assertTrue(created.getActivo());
        verify(spaceRepository).save(created);
    }

    @Test
    void filtersSpacesByTurkishBathType() {
        List<Space> expected = List.of(new Space());
        when(spaceRepository.findByTipo(SpaceType.TURKISH_BATH)).thenReturn(expected);

        assertSame(expected, service.getByType(SpaceType.TURKISH_BATH));
        verify(spaceRepository).findByTipo(SpaceType.TURKISH_BATH);
    }

    @Test
    void createsCustomSpaceAndPreservesItsName() {
        Condominium condominium = new Condominium();
        condominium.setId(10L);

        SpaceRequestDTO request = new SpaceRequestDTO();
        request.setNombre("Salón de yoga");
        request.setTipo(SpaceType.CUSTOM);
        request.setTipoPersonalizado("  Salón de yoga  ");
        request.setCondominioId(10L);

        when(spaceRepository.existsByNombre("Salón de yoga")).thenReturn(false);
        when(condominiumRepository.findById(10L)).thenReturn(Optional.of(condominium));
        when(spaceRepository.save(any(Space.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Space created = service.createSpace(request);

        assertEquals(SpaceType.CUSTOM, created.getTipo());
        assertEquals("Salón de yoga", created.getTipoPersonalizado());
        verify(spaceRepository).save(created);
    }

    @Test
    void rejectsCustomSpaceWithoutAName() {
        SpaceRequestDTO request = new SpaceRequestDTO();
        request.setTipo(SpaceType.CUSTOM);
        request.setTipoPersonalizado("   ");

        assertThrows(
                com.space_reservation.api.exception.BusinessException.class,
                () -> service.createSpace(request)
        );
        verify(spaceRepository, never()).save(any());
    }
}
