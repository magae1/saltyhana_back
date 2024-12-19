package com.saltyhana.saltyhanaserver.dto.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestResultDTO {
    @NotBlank
    private Integer questionNum;
    @NotBlank
    private Integer answerNum;
}