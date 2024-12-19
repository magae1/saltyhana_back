//package com.saltyhana.saltyhanaserver.service;
//
//import com.saltyhana.saltyhanaserver.dto.RecommendResponseDTO;
//import com.saltyhana.saltyhanaserver.entity.Product;
//import com.saltyhana.saltyhanaserver.entity.User;
//import com.saltyhana.saltyhanaserver.mapper.RecommendationMapper;
//import com.saltyhana.saltyhanaserver.repository.ProductRepository;
//import com.saltyhana.saltyhanaserver.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class RecommendationService {
//
//    private final UserRepository userRepository;
//    private final ProductRepository productRepository;
//    private final AnthropicService anthropicService;
//
//    public List<RecommendResponseDTO> getRecommendations(Long userId) {
//        Optional<User> optionalUser = userRepository.findById(userId);
//        if (optionalUser.isEmpty()) {
//            throw new IllegalArgumentException("해당하는 유저가 존재하지 않습니다.");
//        }
//
//        User user = optionalUser.get();
//        if (user.getConsumptionTendency() == null) {
//            // description이 없는 경우 인기 상품 반환
//            List<Product> popularProducts = productRepository.findAllByIdBetween(1L, 6L);
//            return RecommendationMapper.toPopularDTOList(popularProducts); // Rate 정보 필요 없음
//        } else {
//            // description이 있는 경우 Anthropic API 호출
//            String description = user.getConsumptionTendency().getDescription();
//            String birth = user.getBirth().toString();
//
//            // Anthropic API 호출 및 상품명 리스트 추출
//            List<String> productNames = anthropicService.getRecommendations(birth, description);
//
//            // 상품명으로 Product 테이블에서 검색
//            List<Product> matchedProducts = productRepository.findAllByFinPrdtNmIn(productNames);
//
//            // DTO 변환
//            return RecommendationMapper.toTendencyDTOList(matchedProducts, description); // Rate 정보 필요 없음
//        }
//    }
//}
//
//
// 거의 description 제외 완료1
package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.RecommendResponseDTO;
import com.saltyhana.saltyhanaserver.entity.Product;
import com.saltyhana.saltyhanaserver.entity.Rate;
import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.mapper.RecommendationMapper;
import com.saltyhana.saltyhanaserver.repository.ProductRepository;
import com.saltyhana.saltyhanaserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AnthropicService anthropicService;

    public List<RecommendResponseDTO> getRecommendations(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("해당하는 유저가 존재하지 않습니다.");
        }

        User user = optionalUser.get();
        if (user.getConsumptionTendency() == null) {
            // description이 없는 경우 인기 상품 반환
            List<Product> popularProducts = productRepository.findAllByIdBetween(1L, 6L);

            // Rate 정보를 매핑
            Map<Long, Rate> rateMap = mapRatesToProducts(popularProducts);

            return RecommendationMapper.toPopularDTOList(popularProducts, rateMap);
        } else {
            // description이 있는 경우 Anthropic API 호출
            String description = user.getConsumptionTendency().getDescription();
            String birth = user.getBirth().toString();

            // Anthropic API 호출 및 상품명 리스트 추출
            List<String> productNames = anthropicService.getRecommendations(birth, description);

            // 상품명으로 Product 테이블에서 검색
            List<Product> matchedProducts = productRepository.findAllByFinPrdtNmIn(productNames);

            // Rate 정보를 매핑
            Map<Long, Rate> rateMap = mapRatesToProducts(matchedProducts);

            return RecommendationMapper.toTendencyDTOList(matchedProducts, description, rateMap);
        }
    }

    private Map<Long, Rate> mapRatesToProducts(List<Product> products) {
        // Product IDs 추출
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        // Product IDs로 Rate 조회
        List<Rate> rates = productRepository.findRatesByProductIds(productIds);

        // Product ID를 Key로 하는 Map 생성
        return rates.stream()
                .collect(Collectors.toMap(
                        rate -> rate.getProduct().getId(),
                        rate -> rate
                ));
    }
}
