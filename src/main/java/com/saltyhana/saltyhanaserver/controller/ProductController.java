package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.RecommendResponseDTO;
import com.saltyhana.saltyhanaserver.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
// 상품 추천 페이지 Controller
public class ProductController {
    private final RecommendationService recommendationService;

    @PostMapping("/recommend")
    public ResponseEntity<List<RecommendResponseDTO>> getRecommendations(@RequestParam("UserId") Long userId) {
        List<RecommendResponseDTO> recommendations = recommendationService.getRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }

}

