package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.DashBoardResponseDTO;
import com.saltyhana.saltyhanaserver.service.DashboardService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api_prefix}")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @Operation(summary = "대시보드 조회", description = "사용자에 맞는 개인화된 대시보드 정보를 반환합니다.")
    @GetMapping("/home")
    public List<DashBoardResponseDTO> getDashboard(){
        return dashboardService.getGoalsAndWeekdays();
    }
}
