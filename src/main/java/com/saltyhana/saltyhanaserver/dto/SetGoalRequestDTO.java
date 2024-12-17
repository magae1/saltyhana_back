package com.saltyhana.saltyhanaserver.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SetGoalRequestDTO {
    private String goalName;
    private Integer goalMoney;
    private Date startDate;
    private Date endDate;
    private Integer goalType;
    private Long goalIcon;
    private String goalImg;
}