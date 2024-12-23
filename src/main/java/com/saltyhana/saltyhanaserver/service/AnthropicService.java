package com.saltyhana.saltyhanaserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saltyhana.saltyhanaserver.exception.NotFoundException;
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

    private final RestTemplate restTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getRecommendations(String birth, String description) {
        String systemMessage = "당신은 하나은행 전문 금융 컨설턴트입니다.";
        String userMessage = buildPrompt(birth, description);

        HttpEntity<String> entity = createHttpEntity(systemMessage, userMessage);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                throw new NotFoundException("Anthropic API");
            }

            return parseResponse(response.getBody());
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

                사용자 정보:
                - 나이: %s
                - 소비 성향: %s

                %s

                **중요 고려 사항:**
                - 사용자의 나이와 소비 성향에 적합한 상품을 우선적으로 선택.

                응답 형식: "하나 청년도약계좌,급여하나 월복리 적금,정기예금
                9개의 상품만을 응답해줘야만 해.
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
