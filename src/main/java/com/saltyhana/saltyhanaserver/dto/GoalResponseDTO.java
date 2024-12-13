package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class GoalResponseDTO {
    private int id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String imageSrc;
    private int percentage;
}
