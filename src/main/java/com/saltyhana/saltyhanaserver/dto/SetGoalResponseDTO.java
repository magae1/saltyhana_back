package com.saltyhana.saltyhanaserver.dto;

import com.saltyhana.saltyhanaserver.entity.Icon;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class SetGoalResponseDTO {
    private String goalName;
    private Integer goalMoney;    // Long에서 Integer로 변경
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer goalType;
    private Long iconId;   //유저가 선택한 아이콘
    private String goalImg;  //유저가 등록한 이미지
}

