package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.SetGoalRequestDTO;
import com.saltyhana.saltyhanaserver.dto.SetGoalResponseDTO;
import com.saltyhana.saltyhanaserver.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping("/goals")
    public ResponseEntity<SetGoalResponseDTO> createGoal(@RequestBody SetGoalRequestDTO goalDTO) {
        return ResponseEntity.ok(goalService.createGoal(goalDTO));
    }
}