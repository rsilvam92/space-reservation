package com.space_reservation.api.mapper;

import com.space_reservation.api.dto.request.UserRegisterDTO;
import com.space_reservation.api.dto.response.PendingUserDTO;
import com.space_reservation.api.dto.response.UserResponseDTO;
import com.space_reservation.api.entity.User;

public class UserMapper {

    public static User toEntity(UserRegisterDTO dto) {
        if (dto == null) return null;
        User user = new User();
        user.setNombre(dto.getNombre());
        user.setApellido(dto.getApellido());
        user.setCorreo(dto.getCorreo());
        user.setDocumento(dto.getDocumento());
        user.setPassword(dto.getPassword());
        user.setTelefono(dto.getTelefono());
        user.setTipoOcupante(dto.getTipoOcupante());
        return user;
    }

    public static UserResponseDTO toDTO(User user) {
        if (user == null) return null;
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setNombre(user.getNombre());
        dto.setApellido(user.getApellido());
        dto.setCorreo(user.getCorreo());
        dto.setDocumento(user.getDocumento());
        dto.setEstado(user.getEstado() != null ? user.getEstado().name() : null);

        if (user.getRole() != null) {
            dto.setRol(user.getRole().getNombre());
        }

        if (user.getApartment() != null) {
            dto.setApartamentoId(user.getApartment().getId());
            dto.setApartamento(user.getApartment().getSector() + " - " + user.getApartment().getNumero());
        }
        return dto;
    }

    public static PendingUserDTO toPendingDTO(User user){
        PendingUserDTO dto = new PendingUserDTO();
        dto.setId(user.getId());
        dto.setNombreCompleto(
                user.getNombre()+" "+user.getApellido());
        dto.setCorreo(user.getCorreo());
        dto.setApartamento(
                user.getApartment().getSector()+"-"+user.getApartment().getNumero());
        dto.setCondominiumId(user.getApartment().getCondominium().getId());
        dto.setFechaRegistro(user.getCreatedAt());
        return dto;
    }
}
