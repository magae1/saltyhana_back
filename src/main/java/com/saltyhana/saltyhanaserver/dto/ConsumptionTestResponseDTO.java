package com.saltyhana.saltyhanaserver.dto;


import java.util.List;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ConsumptionTestResponseDTO {
    private String question;
    private List<ConsumptionTestAnswerDTO> answers;
}

