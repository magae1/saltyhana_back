package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoalSummaryResponseDTO {
    private Long id;
    private String userName;
    private String title;
    private String goalPeriod;
    private String iconImage;
    private String customImage;
    private Long currentMoney;
    private Long totalMoney;
    private Long percentage;
}
