package com.saltyhana.saltyhanaserver.controller;


import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saltyhana.saltyhanaserver.dto.ConsumptionTestResponseDTO;
import com.saltyhana.saltyhanaserver.dto.ConsumptionTestResultResponseDTO;
import com.saltyhana.saltyhanaserver.dto.form.ConsumptionTestResultForm;


@Log4j2
@RestController
@PreAuthorize("hasAnyRole('ROLE_USER')")
@RequestMapping("${api_prefix}/test")
@RequiredArgsConstructor
public class ConsumptionTestController {

    @PostMapping("/result")
    public void postResult(List<ConsumptionTestResultForm> resultList) {
        return;
    }

    @GetMapping("/result/{id}")
    public ConsumptionTestResultResponseDTO getResult(@PathVariable Long id) {
        return ConsumptionTestResultResponseDTO.builder()
                .build();
    }

    @GetMapping("/${id}")
    public ConsumptionTestResponseDTO getTest(@PathVariable Long id) {
        return ConsumptionTestResponseDTO.builder()
                .build();
    }

}
