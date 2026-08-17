package com.space_reservation.api.controller;

import com.space_reservation.api.dto.response.RegistrationApartmentDTO;
import com.space_reservation.api.dto.response.RegistrationCondominiumDTO;
import com.space_reservation.api.entity.Apartment;
import com.space_reservation.api.entity.Condominium;
import com.space_reservation.api.exception.ResourceNotFoundException;
import com.space_reservation.api.repository.ApartmentRepository;
import com.space_reservation.api.repository.CondominiumRepository;
import com.space_reservation.api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private CondominiumRepository condominiumRepository;
    private ApartmentRepository apartmentRepository;
    private UserController controller;

    @BeforeEach
    void setUp() {
        condominiumRepository = mock(CondominiumRepository.class);
        apartmentRepository = mock(ApartmentRepository.class);
        controller = new UserController(
                mock(UserService.class),
                condominiumRepository,
                apartmentRepository
        );
    }

    @Test
    void getRegistrationCondominiumsReturnsActiveCatalog() {
        Condominium condominium = new Condominium();
        condominium.setId(2L);
        condominium.setNombre("K108 Roble");
        condominium.setCiudad("Bogota");
        when(condominiumRepository.findByActivoTrueOrderByNombreAsc())
                .thenReturn(List.of(condominium));

        List<RegistrationCondominiumDTO> result = controller.getRegistrationCondominiums();

        assertEquals(
                List.of(new RegistrationCondominiumDTO(2L, "K108 Roble", "Bogota")),
                result
        );
    }

    @Test
    void getRegistrationApartmentsReturnsSelectedCondominiumCatalog() {
        Apartment apartment = new Apartment();
        apartment.setId(11L);
        apartment.setSector("Torre 1");
        apartment.setNumero("101");
        apartment.setEstado("DISPONIBLE");
        when(condominiumRepository.existsById(2L)).thenReturn(true);
        when(apartmentRepository.findByCondominiumIdOrderBySectorAscNumeroAsc(2L))
                .thenReturn(List.of(apartment));

        List<RegistrationApartmentDTO> result = controller.getRegistrationApartments(2L);

        assertEquals(
                List.of(new RegistrationApartmentDTO(11L, "Torre 1", "101", "DISPONIBLE")),
                result
        );
    }

    @Test
    void getRegistrationApartmentsRejectsUnknownCondominium() {
        when(condominiumRepository.existsById(999L)).thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> controller.getRegistrationApartments(999L)
        );
        verify(apartmentRepository, never())
                .findByCondominiumIdOrderBySectorAscNumeroAsc(999L);
    }
}
