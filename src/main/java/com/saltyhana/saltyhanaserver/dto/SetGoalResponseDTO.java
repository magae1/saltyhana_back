package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class SetGoalResponseDTO {
    private String goalName;
    private Integer goalMoney;
    private Date startDate;
    private Date endDate;
    private Integer goalType;
    private String goalIcon;
    private String goalImg;
}

