package com.saltyhana.saltyhanaserver.dto;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ConsumptionTestAnswerDTO {
    private String body;
    private Integer seqNum;
}
