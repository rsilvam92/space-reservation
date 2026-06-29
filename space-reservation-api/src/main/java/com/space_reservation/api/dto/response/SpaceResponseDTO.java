package com.space_reservation.api.dto.response;

import com.space_reservation.api.entity.enums.SpaceType;
import lombok.*;

@Getter
@Setter
public class SpaceResponseDTO {

    private Long id;
    private String name;
    private SpaceType type;
}