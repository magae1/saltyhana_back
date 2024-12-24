package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.CalendarResponseDTO;
import com.saltyhana.saltyhanaserver.service.CalendarService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api_prefix}/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @Operation(summary = "목표 목록 조회")
    @GetMapping("/goals")
    public ResponseEntity<List<CalendarResponseDTO>> getAllCalendarGoals() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        List<CalendarResponseDTO> goals = calendarService.getAllCalendarGoals(userId);
        return ResponseEntity.ok(goals);
    }

    @Operation(summary = "목표 삭제", description = "goalId에 해당하는 목표를 삭제합니다.")
    @DeleteMapping("/goals/{goalId}")
    public ResponseEntity<Void> deleteGoalById(@PathVariable Long goalId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        calendarService.deleteGoalById(userId, goalId);
        return ResponseEntity.noContent().build();
    }
}