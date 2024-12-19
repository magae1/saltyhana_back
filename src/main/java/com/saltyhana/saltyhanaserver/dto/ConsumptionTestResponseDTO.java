package com.saltyhana.saltyhanaserver.dto;


import java.util.List;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ConsumptionTestResponseDTO {
    //1.테스트 get으로 받아오기
    private String question;
    private List<ConsumptionTestAnswerDTO> answers;
}

