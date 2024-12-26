package com.saltyhana.saltyhanaserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.saltyhana.saltyhanaserver.entity.RecommendRequest;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
import com.saltyhana.saltyhanaserver.repository.RecommendRequestRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnthropicService {

    @Value("${anthropic.api.url}")
    private String apiUrl;

    @Value("${anthropic.api.key}")
    private String apiKey;

    private final RecommendRequestRepository recommendRequestRepo;
    private final RestTemplate restTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getRecommendations(String birth, String description) {
        Optional<RecommendRequest> cacheResult = recommendRequestRepo.findByBirthAndDescription(
            birth, description);
        if (cacheResult.isPresent()) {
            RecommendRequest recommendRequest = cacheResult.get();
            return parseResponse(recommendRequest.getResponse());
        }

        String systemMessage = "당신은 하나은행 전문 금융 컨설턴트입니다.";
        String userMessage = buildPrompt(birth, description);

        HttpEntity<String> entity = createHttpEntity(systemMessage, userMessage);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new NotFoundException("Anthropic API");
            }

            String resBody = response.getBody();
            recommendRequestRepo.save(RecommendRequest.builder()
                .birth(birth)
                .description(description)
                .response(resBody)
                .build());

            return parseResponse(resBody);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Anthropic API 호출 실패: {}", e.getMessage());
            throw new NotFoundException("Anthropic API");
        }
    }

    private HttpEntity<String> createHttpEntity(String systemMessage, String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("anthropic-version", "2023-06-01");

        String requestBody = createRequestBody(systemMessage, userMessage);
        return new HttpEntity<>(requestBody, headers);
    }

    private String createRequestBody(String systemMessage, String userMessage) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "claude-3-opus-20240229");
            requestBody.put("max_tokens", 3000);
            requestBody.put("system", systemMessage);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", userMessage));
            requestBody.put("messages", messages);

            return objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new RuntimeException("Request Body 생성 실패: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(String birth, String description) {
        List<String> productRates = fetchProductRates();
        String productList = String.join("\n", productRates);

        return String.format(
                """
                 당신은 하나은행 전문 금융 컨설턴트입니다. 사용자의 프로필을 분석하고 제공된 목록에서 가장 적합한 금융 상품을 추천해주세요.
                 상품의 이름만 알려주면 됩니다.
                 
                 **중요한 제한 사항**:
                 - 반드시 아래 제공된 목록에 있는 상품 이름만 사용할 것.
                 - 목록에 없는 상품은 절대 응답하지 말 것.
                 - 상품 이름은 띄어쓰기나 문구를 임의로 수정하지 말 것.
     
                 사용자 정보:
                 - 나이: %s
                 - 소비 성향: %s
     
                 제공된 금융 상품 목록:
                 %s
     
                 **응답 형식**:
                 "상품1,상품2,상품3,... (총 9개)"
                 """,
                birth, description, productList
        );
    }

    private List<String> fetchProductRates() {
        String query = """
                SELECT
                    p.fin_prdt_nm,
                    p.description,
                    r.intr_rate,
                    r.intr_rate2
                FROM
                    product p
                JOIN
                    rate r
                ON
                    p.id = r.product_id
                """;

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            String finPrdtNm = rs.getString("fin_prdt_nm");
            String description = rs.getString("description");
            double intrRate = rs.getDouble("intr_rate");
            double intrRate2 = rs.getDouble("intr_rate2");

            return String.format("%s: %s 금리: %.2f%%~%.2f%%",
                    finPrdtNm, description, intrRate, intrRate2);
        });
    }


    private List<String> parseResponse(String responseBody) {
        try {
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

            List<Map<String, Object>> contentList = (List<Map<String, Object>>) responseMap.get("content");
            if (contentList == null || contentList.isEmpty()) {
                throw new NotFoundException("Anthropic API 응답");
            }

            StringBuilder fullContent = new StringBuilder();
            for (Map<String, Object> contentItem : contentList) {
                if ("text".equals(contentItem.get("type"))) {
                    fullContent.append(contentItem.get("text"));
                }
            }

            String content = fullContent.toString();
            return Arrays.stream(content.split(",|\\n"))
                    .map(String::trim)
                    .filter(item -> !item.isEmpty())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("API 응답 파싱 실패: {}", e.getMessage());
            throw new NotFoundException("Anthropic API 응답");
        }
    }
}
