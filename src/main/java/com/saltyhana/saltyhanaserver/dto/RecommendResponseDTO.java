package com.saltyhana.saltyhanaserver.dto;

public class RecommendResponseDTO {
    private int type;       // "asset" - 0 | "tendency" - 1 (자산 기반 추천인지 성향 테스트 기반 추천인지)
    private String title;
    private String subTitle;
    private String imageUrl;
    private String description;

    private String tendency;    // String | null   (차분하고 어쩌고) | (테스트 전)

}
