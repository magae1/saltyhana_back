package com.saltyhana.saltyhanaserver.dto;


import com.saltyhana.saltyhanaserver.enums.ProductEnum;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RecommendResponseDTO {
    private ProductEnum type;       // "asset" - 0 | "tendency" - 1 (자산 기반 추천인지 성향 테스트 기반 추천인지)
    private String title;
    private String subTitle;
    private String imageUrl;
    private String description;
    private String name;
    private String tendency;    // String | null   (차분하고 어쩌고) | (테스트 전)
    private String link;        // 상품 링크
    private String reason;      // 상품들 추천 이유
}
