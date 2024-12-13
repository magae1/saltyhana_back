package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DashBoardResponseDTO {
    private List<GoalResponseDTO> goal;
    private List<WeekdayCalendarResponseDTO> weekday;
    private List<BestProductListResponseDTO> bestProductList;
}
