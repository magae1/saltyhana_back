package com.saltyhana.saltyhanaserver.controller;


import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saltyhana.saltyhanaserver.dto.form.ConsumptionTestResultFormDTO;
import com.saltyhana.saltyhanaserver.service.ConsumptionTestService;
import com.saltyhana.saltyhanaserver.dto.ConsumptionTestResponseDTO;
import com.saltyhana.saltyhanaserver.dto.ConsumptionTestResultResponseDTO;


@Log4j2
@RestController
@RequestMapping("${api_prefix}/test")
@RequiredArgsConstructor
public class ConsumptionTestController {
    private final ConsumptionTestService consumptionTestService;

    @Operation(summary = "소비성향 테스트 조회", description = "test_id에 대한 소비성향 테스트 내용을 반환합니다.")
    @GetMapping("/{id}")
    public ConsumptionTestResponseDTO getTest(@PathVariable Long id) {
        return consumptionTestService.getPage(id);
    }

    @Operation(summary = "소비성향 테스트 요청", description = "소비성향 테스트 결과를 전송합니다.")
    @PostMapping("/result")
    public void postResult(@RequestBody List<ConsumptionTestResultFormDTO> resultForm) {
        Long userId = getUserId();
        consumptionTestService.receiveResult(userId, resultForm);
    }

    @Operation(summary = "소비성향 조회", description = "사용자의 소비성향을 조회합니다.")
    @GetMapping("/result")
    public ConsumptionTestResultResponseDTO getResult() {
        Long userId = getUserId();
        return consumptionTestService.getTendency(userId);
    }

    private Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getPrincipal();
    }
}
