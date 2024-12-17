package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.SetGoalRequestDTO;
import com.saltyhana.saltyhanaserver.dto.SetGoalResponseDTO;
import com.saltyhana.saltyhanaserver.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<SetGoalResponseDTO> createGoal(@RequestBody SetGoalRequestDTO goalDTO) {
        // SecurityContext에서 직접 userId를 가져오므로 token 파라미터가 필요 없음
        return ResponseEntity.ok(goalService.createGoal(goalDTO));
    }
}