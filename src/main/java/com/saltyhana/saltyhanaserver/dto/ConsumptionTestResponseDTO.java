package com.saltyhana.saltyhanaserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsumptionTestResponseDTO {

    //output
    private Long id; //소비유형 id(1~3)
    private String title; //예: "마지막에 웃는 진짜 승리자!"
    private String description; //예: 당장의 소비를 참고 자산을 관리하는 당신! 부자가 될 자격이 있습니다.
    private String type; // "YONO"
    private String mbti; // "___J"
    private String emoji; // 이미지url

}
