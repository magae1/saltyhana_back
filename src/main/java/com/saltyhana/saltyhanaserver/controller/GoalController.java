package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.GoalResponseDTO;
import com.saltyhana.saltyhanaserver.dto.SetGoalDTO;
import com.saltyhana.saltyhanaserver.service.GoalService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api_prefix}/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @Operation(summary = "새 목표 추가")
    @PostMapping
    public ResponseEntity<SetGoalDTO> createGoal(@RequestBody SetGoalDTO goalDTO) {
        return ResponseEntity.ok(goalService.createOrUpdateGoal(goalDTO, null));
    }

    @Operation(summary = "목표 목록 조회", description = "사용자의 목표 목록을 조회힙니다.")
    @GetMapping
    public ResponseEntity<List<GoalResponseDTO>> getGoals(
            @RequestParam(name = "activeOnly", required = false, defaultValue = "false") boolean activeOnly) {
        return ResponseEntity.ok(goalService.getGoals(activeOnly));
    }

    @Operation(summary = "목표 수정", description = "goalId에 맞는 목표를 수정합니다.")
    @PutMapping("/{goalId}")
    public ResponseEntity<SetGoalDTO> updateGoal(
            @PathVariable Long goalId, @RequestBody SetGoalDTO goalDTO) {
        return ResponseEntity.ok(goalService.createOrUpdateGoal(goalDTO, goalId));
    }
}