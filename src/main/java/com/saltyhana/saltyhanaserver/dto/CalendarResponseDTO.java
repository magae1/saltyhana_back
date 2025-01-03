package com.saltyhana.saltyhanaserver.dto;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CalendarResponseDTO {
    private Long id;
    private String title;
    private String startDate;
    private String endDate;
    private String icon;
    private String color;
}
