package com.space_reservation.api.dto.response;

import com.space_reservation.api.entity.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationResponseDTO {

    private Long id;
    private String userName;
    private String spaceName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationStatus status;
    private boolean confirmed;
}