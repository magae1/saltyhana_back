package com.saltyhana.saltyhanaserver.dto;


public class RecommendResponseDTO {
    private int type;   // "asset" - 0 | "tendency" - 1 (자산 기반 추천인지 성향 테스트 기반 추천인지)
    private String title;
    private String subTitle;
    private String imageUrl;    // 카드 안에 들어갈 로고 이미지
    private String description;

    private String tendency     // 성향 테스트 결과 (String || null)
}