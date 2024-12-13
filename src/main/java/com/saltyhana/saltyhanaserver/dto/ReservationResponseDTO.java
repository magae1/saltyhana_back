package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationResponseDTO {
    private String userId;
    private LocalDateTime date;
    private String place;
}
