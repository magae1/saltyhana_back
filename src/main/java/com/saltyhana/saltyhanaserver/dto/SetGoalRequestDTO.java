package com.saltyhana.saltyhanaserver.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SetGoalRequestDTO {
    private String goalName;
    private Integer goalMoney;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer goalType;
    private Long iconId;   //유저가 선택한 아이콘
    private String goalImg;  //유저가 등록한 이미지
    private Long connectedAccount;
}