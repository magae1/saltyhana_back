package com.saltyhana.saltyhanaserver.dto;

import com.saltyhana.saltyhanaserver.entity.Icon;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class SetGoalResponseDTO {
    private String goalName;
    private Integer goalMoney;    // Long에서 Integer로 변경
    private Date startDate;       // LocalDateTime에서 Date로 변경
    private Date endDate;         // LocalDateTime에서 Date로 변경
    private Integer goalType;
    private Long goalIcon;
    private String goalImg;
}

