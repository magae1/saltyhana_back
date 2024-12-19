package com.saltyhana.saltyhanaserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class AnthropicConfig {

    @Value("${anthropic.api.key}")
    private String apiKey;

    @Value("${anthropic.api.version:2023-06-01}")  // 기본값 설정
    private String apiVersion;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(20000);
        factory.setReadTimeout(20000);

        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(factory));

        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            request.getHeaders().set("x-api-key", apiKey);  // 변경된 부분
            request.getHeaders().set("anthropic-version", apiVersion);

            System.out.println("Request Body: " + new String(body));
            if (body == null || body.length == 0) {
                log.info("Request Body is empty");
            } else {
                log.info("Request Body: " + new String(body));
            }
            return execution.execute(request, body);
        });

        return restTemplate;
    }

}
