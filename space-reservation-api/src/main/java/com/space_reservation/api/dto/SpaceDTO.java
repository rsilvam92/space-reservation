package com.space_reservation.api.dto;

import com.space_reservation.api.entity.enums.SpaceType;
import lombok.*;

@Getter
@Setter
public class SpaceDTO {

    private String name;
    private SpaceType type;
    private Integer maxHours;
    private Integer maxWeeklyReservations;
}