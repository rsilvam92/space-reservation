package com.space_reservation.api.mapper;

import com.space_reservation.api.dto.response.PendingUserDTO;
import com.space_reservation.api.entity.Apartment;
import com.space_reservation.api.entity.Condominium;
import com.space_reservation.api.entity.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    @Test
    void toPendingDtoIncludesCondominiumId() {
        Condominium condominium = new Condominium();
        condominium.setId(2L);

        Apartment apartment = new Apartment();
        apartment.setSector("Torre 7");
        apartment.setNumero("203");
        apartment.setCondominium(condominium);

        User user = new User();
        user.setId(18L);
        user.setNombre("Jose");
        user.setApellido("Figueroa");
        user.setCorreo("josef@gmail.com");
        user.setApartment(apartment);
        user.setCreatedAt(LocalDateTime.of(2026, 8, 17, 9, 0));

        PendingUserDTO result = UserMapper.toPendingDTO(user);

        assertEquals(2L, result.getCondominiumId());
        assertEquals("Jose Figueroa", result.getNombreCompleto());
        assertEquals("Torre 7-203", result.getApartamento());
    }
}
