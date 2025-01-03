package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashBoardResponseDTO {
    private GoalSummaryResponseDTO goal;
    private WeekdayCalendarResponseDTO weekdayCalendar;
    private List<BestProductListResponseDTO> bestProductList;
}
