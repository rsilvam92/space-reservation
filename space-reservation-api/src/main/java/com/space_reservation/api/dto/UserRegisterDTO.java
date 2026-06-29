package com.space_reservation.api.dto;

import lombok.*;

@Getter
@Setter
public class UserRegisterDTO {

    private String fullName;
    private String email;
    private String documentNumber;
    private String password;
    private Long apartmentId;
}