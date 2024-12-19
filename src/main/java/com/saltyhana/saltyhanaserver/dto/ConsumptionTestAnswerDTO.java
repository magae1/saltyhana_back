package com.saltyhana.saltyhanaserver.dto;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ConsumptionTestAnswerDTO {
    private String body; //질문
    private Integer seqNum; //몇번 골랐는지? 몇번문제인지?
}
