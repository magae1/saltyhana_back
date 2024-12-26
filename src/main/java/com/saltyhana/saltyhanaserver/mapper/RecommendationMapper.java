package com.saltyhana.saltyhanaserver.mapper;

import com.saltyhana.saltyhanaserver.dto.RecommendResponseDTO;
import com.saltyhana.saltyhanaserver.entity.Product;
import com.saltyhana.saltyhanaserver.entity.Rate;
import com.saltyhana.saltyhanaserver.enums.ProductEnum;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RecommendationMapper {

    // 금리와 저축 기간 형식화
    public static String formatDescription(Rate rate) {
        if (rate == null) {
            return "저축기간: 정보 없음, 금리: 정보 없음";
        }
        String saveTrm = rate.getSaveTrm() != null ? rate.getSaveTrm() + "개월" : "정보 없음";
        String intrRate = rate.getIntrRate() != null ? rate.getIntrRate() + "%" : "정보 없음";
        String intrRate2 = rate.getIntrRate2() != null ? rate.getIntrRate2() + "%" : "정보 없음";
        return String.format("저축기간: %s, 금리: %s ~ %s", saveTrm, intrRate, intrRate2);
    }

    /// 상품 추천 페이지 부분
    public static List<RecommendResponseDTO> toPopularDTOList(List<Product> products, Map<Long, Rate> rateMap, String userName, String tendency) {
        return products.stream()
                .map(product -> RecommendResponseDTO.builder()
                        .type(ProductEnum.POPULAR) // 타입을 POPULAR로 유지
                        .title(product.getFinPrdtNm())
                        .subTitle(product.getSpclCnd()) // 상품의 부제목 설정
                        .imageUrl("https://example.com/image/" + product.getId()) // 이미지 URL 생성
                        .description(formatDescription(rateMap.get(product.getId()))) // 금리 정보 추가
                        .name(userName) // 사용자 이름 추가
                        .tendency(tendency) // 소비 성향 추가
                        .link(product.getLinkPrd())
                        .build())
                .collect(Collectors.toList());
    }


    public static List<RecommendResponseDTO> toTendencyDTOList(List<Product> products, String tendency, Map<Long, Rate> rateMap, String userName) {
        return products.stream()
                .map(product -> RecommendResponseDTO.builder()
                        .type(ProductEnum.TENDENCY)
                        .title(product.getFinPrdtNm())
                        .subTitle(product.getSpclCnd())
                        .imageUrl("https://example.com/image/" + product.getId())
                        .description(formatDescription(rateMap.get(product.getId())))
                        .name(userName) // 사용자 이름 설정
                        .tendency(tendency)
                        .link(product.getLinkPrd())
                        .build())
                .collect(Collectors.toList());
    }
}



