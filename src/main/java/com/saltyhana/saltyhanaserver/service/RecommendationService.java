package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.RecommendResponseDTO;
import com.saltyhana.saltyhanaserver.entity.Product;
import com.saltyhana.saltyhanaserver.entity.Rate;
import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.mapper.RecommendationMapper;
import com.saltyhana.saltyhanaserver.repository.ProductRepository;
import com.saltyhana.saltyhanaserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AnthropicService anthropicService;
    private final RestTemplate restTemplate;

    public List<RecommendResponseDTO> getRecommendations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new IllegalArgumentException("해당하는 유저가 존재하지 않습니다.");
                });

        String userName = user.getName();

        // 성향 존재 여부
        if (user.getConsumptionTendency() == null) {
            return getPopularRecommendations(user.getName(), null); // 소비 성향이 없는 경우
        } else {
            return getPersonalizedRecommendations(user, user.getName());
        }
    }


    private List<RecommendResponseDTO> getPopularRecommendations(String userName, String tendency) {
        List<Product> popularProducts = productRepository.findAllByIdBetween(1L, 6L); // 인기 상품 조회
        Map<Long, Rate> rateMap = mapRatesToProducts(popularProducts); // 금리 정보 매핑

        log.info("Returning popular recommendations for products: {}", popularProducts);
        return RecommendationMapper.toPopularDTOList(popularProducts, rateMap, userName, tendency);
    }


    /**
     * 소비 성향이 있는 경우 Anthropic API 호출 후 추천 상품 반환
     */
    private List<RecommendResponseDTO> getPersonalizedRecommendations(User user, String userName) {
        String description = user.getConsumptionTendency().getDescription();
        String birth = user.getBirth().toString();

//        log.info("Calling Anthropic API for user: {}, Description: {}", userName, description);

        // Anthropic API 호출
        List<String> productNames = anthropicService.getRecommendations(birth, description);

        // 상품명으로 Product 테이블에서 검색
        List<Product> matchedProducts = productRepository.findAllByFinPrdtNmIn(productNames);

        // 이율 Mapping
        Map<Long, Rate> rateMap = mapRatesToProducts(matchedProducts);

//        log.info("Returning personalized recommendations for user: {}, Matched Products: {}", userName, matchedProducts);
        return RecommendationMapper.toTendencyDTOList(matchedProducts, description, rateMap, userName);
    }


    /**
     * Product와 연결된 Rate를 Map 형태로 변환
     */
    private Map<Long, Rate> mapRatesToProducts(List<Product> products) {
        List<Long> productIds = products.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        List<Rate> rates = productRepository.findRatesByProductIds(productIds);

        return rates.stream()
                .collect(Collectors.toMap(
                        rate -> rate.getProduct().getId(),
                        rate -> rate
                ));
    }
}

