package com.saltyhana.saltyhanaserver.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saltyhana.saltyhanaserver.service.ConsumptionTestService;
import com.saltyhana.saltyhanaserver.dto.ConsumptionTestResponseDTO;
import com.saltyhana.saltyhanaserver.dto.ConsumptionTestResultResponseDTO;
import com.saltyhana.saltyhanaserver.dto.form.ConsumptionTestResultForm;


@Log4j2
@RestController
@RequestMapping("${api_prefix}/test")
@RequiredArgsConstructor
public class ConsumptionTestController {
    private final ConsumptionTestService consumptionTestService;

    //페이지 하나
    @GetMapping("/{id}")
    public ConsumptionTestResponseDTO getTest(@PathVariable Long id) {
        return consumptionTestService.getPage(id);
    }

    //응답 하나씩 보내기
    @PostMapping("/result")
    public void postResult(ConsumptionTestResultForm result) {
        Long userId = getUserId();
        consumptionTestService.sendResult(userId, result);
    }

    //마지막 결과
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
