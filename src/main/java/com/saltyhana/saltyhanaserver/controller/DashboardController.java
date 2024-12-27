package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.DashBoardResponseDTO;
import com.saltyhana.saltyhanaserver.dto.GoalAchievementDTO;
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

    @Operation(summary = "확인되지 않은 목표 달성 여부 조회", description = "종료된 목표 중 확인되지 않은 목표를 반환합니다.")
    @GetMapping("/home/achieve/unchecked")
    public List<GoalAchievementDTO> getUncheckedAchievements() {
        return dashboardService.getUncheckedAchievements();
    }

    @Operation(summary = "목표 확인 상태 저장", description = "사용자가 확인한 목표를 Redis에 저장합니다.")
    @PostMapping("/home/achieve/confirm")
    public void confirmAchievements(@RequestBody List<Long> goalIds) {
        dashboardService.confirmAchievements(goalIds);
    }
}
