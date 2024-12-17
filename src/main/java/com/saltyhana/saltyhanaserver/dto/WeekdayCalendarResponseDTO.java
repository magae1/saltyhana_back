package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class WeekdayCalendarResponseDTO {
    private List<WeekDayType> weekday;
}
