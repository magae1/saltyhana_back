package com.saltyhana.saltyhanaserver.service;

import com.saltyhana.saltyhanaserver.dto.BestProductListResponseDTO;
import com.saltyhana.saltyhanaserver.dto.RecommendResponseDTO;
import com.saltyhana.saltyhanaserver.entity.ConsumptionTendency;
import com.saltyhana.saltyhanaserver.entity.Product;
import com.saltyhana.saltyhanaserver.entity.Rate;
import com.saltyhana.saltyhanaserver.entity.User;
import com.saltyhana.saltyhanaserver.enums.ProductEnum;
import com.saltyhana.saltyhanaserver.exception.BadRequestException;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.mapper.RecommendationMapper;
import com.saltyhana.saltyhanaserver.repository.ProductRepository;
import com.saltyhana.saltyhanaserver.repository.RateRepository;
import com.saltyhana.saltyhanaserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AnthropicService anthropicService;
    private final RestTemplate restTemplate;
    private final RateRepository rateRepository;

    public List<RecommendResponseDTO> getRecommendations(Long userId)
        throws ResponseStatusException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new BadRequestException();
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


    private List<RecommendResponseDTO> getPersonalizedRecommendations(User user, String userName) {
        String description = Optional.ofNullable(user.getConsumptionTendency())
                .map(ConsumptionTendency::getDescription)
                .orElse("소비 성향이 제공되지 않았습니다.");

        String birth = user.getBirth().toString();
        List<Map<String, String>> productData = anthropicService.getRecommendations(birth, description);
        log.info("AnthropicService returned product data: {}", productData);

        if (productData.isEmpty()) {
            log.warn("No product data returned from API for user: {}", userName);
            return getPopularRecommendations(userName, null);
        }

        String productNameString = productData.get(0).get("productName");
        List<String> productNames = Arrays.stream(productNameString.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        List<Product> matchedProducts = productRepository.findAllByFinPrdtNmIn(productNames);
        Map<Long, Rate> rateMap = mapRatesToProducts(matchedProducts);

        String reason = productData.get(0).get("reason"); // 단일 이유만 추출

        return RecommendationMapper.toTendencyDTOList(matchedProducts, description, rateMap, userName, reason);
    }


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

    //대시보드에 제공할 기본추천 상품
    protected List<BestProductListResponseDTO> getBestProductList() {

        Pageable pageable = PageRequest.of(0, 5);
        List<Product> bestProductList = productRepository.findBestProductList(pageable);

        // 추천 상품이 없을 경우 예외 처리
        if (bestProductList == null || bestProductList.isEmpty()) {
            throw new NotFoundException("추천상품");
        }

        return bestProductList.stream()
                .map(product -> {
                    Rate rate = rateRepository.findByProductId(product.getId());

                    return BestProductListResponseDTO.builder()
                            .id(product.getId())
                            .type(ProductEnum.ASSET)
                            .title(product.getFinPrdtNm())
                            .subtitle(product.getSpclCnd())
                            .imageUrl("https://example.com/image/" + product.getId())
                            .description(RecommendationMapper.formatDescription(rate))
                            .productLink(product.getLinkPrd())
                            .build();
                })
                .collect(Collectors.toList());
    }
}

