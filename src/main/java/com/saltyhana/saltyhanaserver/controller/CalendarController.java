package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.CalendarResponseDTO;
import com.saltyhana.saltyhanaserver.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    //모든 목표 조회
    @GetMapping("/goals")
    public ResponseEntity<List<CalendarResponseDTO>> getAllCalendarGoals() {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Long userId = (Long) auth.getPrincipal();

        List<CalendarResponseDTO> goals = calendarService.getAllCalendarGoals(111L);
        return ResponseEntity.ok(goals);
    }

    // 특정 목표(goalId) 삭제
    @DeleteMapping("/goals/{goalId}")
    public ResponseEntity<Void> deleteGoalById(@PathVariable Long goalId) {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //Long userId = (Long) auth.getPrincipal();

        calendarService.deleteGoalById(111L, goalId);
        return ResponseEntity.noContent().build();
    }
}