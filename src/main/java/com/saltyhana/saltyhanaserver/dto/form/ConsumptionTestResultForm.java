package com.saltyhana.saltyhanaserver.dto.form;


import com.saltyhana.saltyhanaserver.entity.ConsumptionTestAnswer;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ConsumptionTestResultForm {
    //FE에서 오는 형식. 답변 하나하나를 보낸다고 가정하고 관련코드 작성.

    @NotBlank
    private Integer questionNum;
    @NotBlank
    private Integer answerNum;
}
