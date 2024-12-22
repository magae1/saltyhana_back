package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.CalendarResponseDTO;
import com.saltyhana.saltyhanaserver.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api_prefix}/calendar")
@PreAuthorize("hasAnyRole('ROLE_USER')")
public class CalendarController {

    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    //모든 목표 조회
    @GetMapping("/goals")
    public ResponseEntity<List<CalendarResponseDTO>> getAllCalendarGoals() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        List<CalendarResponseDTO> goals = calendarService.getAllCalendarGoals(userId);
        return ResponseEntity.ok(goals);
    }

    // 특정 목표(goalId) 삭제
    @DeleteMapping("/goals/{goalId}")
    public ResponseEntity<Void> deleteGoalById(@PathVariable Long goalId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) auth.getPrincipal();

        calendarService.deleteGoalById(userId, goalId);
        return ResponseEntity.noContent().build();
    }
}