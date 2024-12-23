package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.RecommendResponseDTO;
import com.saltyhana.saltyhanaserver.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api_prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final RecommendationService recommendationService;

    @GetMapping("/recommend")
    public ResponseEntity<List<RecommendResponseDTO>> getRecommendations() {
        Long userId = getUserId();

        log.info("Authenticated User ID: {}", userId);

        List<RecommendResponseDTO> recommendations = recommendationService.getRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }

    private Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal(); // Principal에서 사용자 ID(Long) 반환
    }
}