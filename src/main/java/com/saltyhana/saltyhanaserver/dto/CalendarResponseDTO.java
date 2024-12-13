package com.saltyhana.saltyhanaserver.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CalendarResponseDTO {
    private int id;
    private String title;
    private String startDate;
    private String endDate;
    private String color;
}
