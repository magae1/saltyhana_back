package com.saltyhana.saltyhanaserver.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsumptionTestAnswerWithScoreDTO {
    private Integer questionNum;
    private Integer answerNum;
    private Integer score;
}
