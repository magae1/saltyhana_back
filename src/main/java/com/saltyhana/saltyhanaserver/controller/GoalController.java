package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.GoalResponseDTO;
import com.saltyhana.saltyhanaserver.dto.SetGoalDTO;
import com.saltyhana.saltyhanaserver.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api_prefix}/goals")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_USER')")
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<SetGoalDTO> createGoal(@RequestBody SetGoalDTO goalDTO) {
        return ResponseEntity.ok(goalService.createOrUpdateGoal(goalDTO, null));
    }

    @GetMapping
    public ResponseEntity<List<GoalResponseDTO>> getGoals(
            @RequestParam(name = "activeOnly", required = false, defaultValue = "false") boolean activeOnly) {
        return ResponseEntity.ok(goalService.getGoals(activeOnly));
    }

    @PutMapping("/{goalId}")
    public ResponseEntity<SetGoalDTO> updateGoal(
            @PathVariable Long goalId, @RequestBody SetGoalDTO goalDTO) {
        return ResponseEntity.ok(goalService.createOrUpdateGoal(goalDTO, goalId));
    }
}