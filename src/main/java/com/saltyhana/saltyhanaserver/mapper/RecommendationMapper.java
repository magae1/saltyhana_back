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

    // ASSET 타입 매핑
//    public static RecommendResponseDTO toAssetDTO(Recommended recommended, Rate rate) {
//        return RecommendResponseDTO.builder()
//                .type(ProductEnum.ASSET)
//                .title(recommended.getId().getProduct().getFinPrdtNm())
//                .subTitle(recommended.getId().getProduct().getJoinMember()) // 변할 수 있음 -> 확장
//                .imageUrl("https://example.com/image/" + recommended.getId().getProduct().getId())
//                .description(formatDescription(rate))
//                .name(recommended.getId().getUser().getName())
//                .tendency(null)
//                .build();
//    }


    // TENDENCY 타입 매핑
//    public static RecommendResponseDTO toTendencyDTO(Recommended recommended, Rate rate, String tendencyDescription) {
//        return RecommendResponseDTO.builder()
//                .type(ProductEnum.TENDENCY)
//                .title(recommended.getId().getProduct().getFinPrdtNm())
//                .subTitle(recommended.getId().getProduct().getFinPrdtNm())
//                .imageUrl("https://example.com/image/" + recommended.getId().getProduct().getId())
//                .description(formatDescription(rate))
//                .name(recommended.getId().getUser().getName())
//                .tendency(tendencyDescription)
//                .build();
//    }

    // 리스트 매핑: 자산 기반 추천은 항상 반환되고, tendency가 있으면 소비 성향도 추가
    public static List<RecommendResponseDTO> toRecommendationList(List<Object[]> resultList, String tendencyDescription) {
        Map<Long, RecommendResponseDTO> resultMap = new LinkedHashMap<>();

        // 자산 기반 추천 (ASSET) - 항상 추가됨
//        resultList.forEach(result -> {
//            Recommended recommended = (Recommended) result[0];
//            Rate rate = (Rate) result[1];
//            resultMap.putIfAbsent(recommended.getId().getProduct().getId(), toAssetDTO(recommended, rate));
//        });

        // 소비 성향 추천 (TENDENCY) - tendencyDescription이 있을 경우만 추가됨
        if (tendencyDescription != null) {
//            resultList.forEach(result -> {
//                Recommended recommended = (Recommended) result[0];
//                Rate rate = (Rate) result[1];
//                resultMap.put(recommended.getId().getProduct().getId(),
//                        toTendencyDTO(recommended, rate, tendencyDescription));
//            });
        }

        return resultMap.values().stream().collect(Collectors.toList());
    }

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
    public static List<RecommendResponseDTO> toPopularDTOList(List<Product> products, Map<Long, Rate> rateMap) {
        return products.stream()
                .map(product -> RecommendResponseDTO.builder()
                        .type(ProductEnum.POPULAR)
                        .title(product.getFinPrdtNm())
                        .subTitle(product.getJoinMember())      // 수정 필요
                        .imageUrl("https://example.com/image/" + product.getId())   // 수정 필요
                        .description(formatDescription(rateMap.get(product.getId()))) // 금리 정보 추가
                        .build())
                .collect(Collectors.toList());
    }

    /// 상품 추천 페이지 부분
    public static List<RecommendResponseDTO> toTendencyDTOList(List<Product> products, String tendency, Map<Long, Rate> rateMap) {
        return products.stream()
                .map(product -> RecommendResponseDTO.builder()
                        .type(ProductEnum.TENDENCY)
                        .title(product.getFinPrdtNm())
                        .subTitle(product.getJoinMember())
                        .imageUrl("https://example.com/image/" + product.getId())
                        .description(formatDescription(rateMap.get(product.getId())) + " " + tendency)
                        .build())
                .collect(Collectors.toList());
    }


}


