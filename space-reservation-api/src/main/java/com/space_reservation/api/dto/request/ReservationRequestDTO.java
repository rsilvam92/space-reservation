package com.space_reservation.api.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationRequestDTO {

    private Long userId;
    private Long spaceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}