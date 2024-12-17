//package com.saltyhana.saltyhanaserver.service;
//
//import com.saltyhana.saltyhanaserver.dto.RecommendResponseDTO;
//import com.saltyhana.saltyhanaserver.entity.Product;
//import com.saltyhana.saltyhanaserver.entity.Recommended;
//import com.saltyhana.saltyhanaserver.enums.ProductEnum;
//import com.saltyhana.saltyhanaserver.repository.ProductRepository;
//import com.saltyhana.saltyhanaserver.repository.RecommendedRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class RecommendationService {
//    private final RecommendedRepository recommendedRepository;
//    private final ProductRepository productRepository;
//
//    // 자산 기반 추천
//    public List<RecommendResponseDTO> getAssetRecommendations(Long userId) {
//        List<Recommended> recommendations = recommendedRepository.findById_User_Id(userId);
//
//        return recommendations.stream().map(rec -> {
//            Product product = rec.getId().getProduct();
//            return RecommendResponseDTO.builder()
//                    .type(ProductEnum.ASSET)
//                    .title(product.getFinPrdtNm())
//                    .subTitle(product.getKorCoNm())
//                    .imageUrl("https://example.com/image/" + product.getId())
//                    .description(product.getSpclCnd())
//                    .tendency(null)
//                    .build();
//        }).collect(Collectors.toList());
//    }
//
//    // 소비 성향 기반 추천
//    public List<RecommendResponseDTO> getTendencyRecommendations(String tendencyDescription) {
//        return List.of(RecommendResponseDTO.builder()
//                .type(ProductEnum.TENDENCY)
//                .title("소비 성향에 맞는 추천 상품")
//                .subTitle("당신의 성향: " + tendencyDescription)
//                .imageUrl("https://example.com/tendency-image")
//                .description("테스트 결과를 바탕으로 상품을 추천합니다.")
//                .tendency(tendencyDescription)
//                .build());
//    }
//
//    // 두 추천 데이터를 함께 반환
//    public List<RecommendResponseDTO> getAllRecommendations(Long userId, String tendencyDescription) {
//        List<RecommendResponseDTO> assetRecommendations = getAssetRecommendations(userId);
//        List<RecommendResponseDTO> tendencyRecommendations = getTendencyRecommendations(tendencyDescription);
//        assetRecommendations.addAll(tendencyRecommendations); // 두 리스트 병합
//        return assetRecommendations;
//    }
//}
