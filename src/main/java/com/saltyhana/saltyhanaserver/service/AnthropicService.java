package com.saltyhana.saltyhanaserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnthropicService {

    @Value("${anthropic.api.url}")
    private String apiUrl;

    @Value("${anthropic.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<String> getRecommendations(String birth, String description) {
        String systemMessage = "당신은 하나은행 전문 금융 컨설턴트입니다.";
        String userMessage = buildPrompt(birth, description);

        HttpEntity<String> entity = createHttpEntity(systemMessage, userMessage);

        try {
            log.info("Requesting Anthropic API: URL={}", apiUrl);
            log.info("Request Body: {}", entity.getBody());

            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
            log.info("Anthropic Response: {}", response.getBody());

            return parseResponse(response.getBody());
        } catch (Exception e) {
            log.error("Anthropic API 호출 실패: {}", e.getMessage());
            throw new RuntimeException("Anthropic API 호출 실패: " + e.getMessage(), e);
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
            // 최상위 system 메시지와 messages 배열 생성
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "claude-3-opus-20240229");
            requestBody.put("max_tokens", 100);
            requestBody.put("system", systemMessage);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", userMessage));
            requestBody.put("messages", messages);

            // JSON 문자열로 변환
            return objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            throw new RuntimeException("Request Body 생성 실패: " + e.getMessage(), e);
        }
    }

    private String buildPrompt(String birth, String description) {
        String productList = """
        하나은행 금융상품 목록:
        1. 영하나플러스 - YOUTH 전용 수수료 우대 서비스 제공
        2. 급여하나플러스 - 급여 이체 우대 서비스 제공
        3. 네이버페이 머니 하나 통장 - 네이버페이 우대 서비스 제공
        4. 하나청년도약계좌 - 청년 자산형성 지원 계좌
        5. 트래블로그 여행적금 - 여행 자금 목돈 마련
        6. 도전 365 적금 - 걸음수 우대 금리 제공
        7. 하나 빌리언달러 통장 - 외화 다통화 입출금 통장
        """;

        return String.format(
                """
                사용자의 프로필을 분석하고 제공된 목록에서 가장 적합한 금융 상품을 추천해주세요.
                상품의 타이틀만 넘겨줘. 부연설명은 필요가 없어
                상품 3개 보여줘
                
                사용자 정보:
                - 나이: %s
                - 소비 성향: %s
        
                %s
        
                응답 형식: "하나 청년도약계좌,급여하나 월복리 적금,정기예금"
                """,
                birth, description, productList
        );
    }


    private List<String> parseResponse(String responseBody) {
        try {
            Map<String, Object> responseMap = objectMapper.readValue(responseBody, Map.class);

            // content 필드에서 데이터를 추출
            List<Map<String, Object>> contentList = (List<Map<String, Object>>) responseMap.get("content");
            if (contentList == null || contentList.isEmpty()) {
                throw new RuntimeException("API 응답에서 'content' 필드를 찾을 수 없습니다.");
            }

            StringBuilder fullContent = new StringBuilder();
            for (Map<String, Object> contentItem : contentList) {
                if ("text".equals(contentItem.get("type"))) {
                    fullContent.append(contentItem.get("text"));
                }
            }

            String content = fullContent.toString();
            log.info("Anthropic API Content: {}", content);

            return Arrays.asList(content.split(","));
        } catch (Exception e) {
            throw new RuntimeException("API 응답 파싱 실패: " + e.getMessage(), e);
        }
    }


}
