package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class WeekDayType {
    private LocalDate date;
    private Boolean isAchieve;
}
