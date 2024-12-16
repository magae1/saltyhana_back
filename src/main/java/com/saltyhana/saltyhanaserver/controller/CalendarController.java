package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.dto.CalendarResponseDTO;
import com.saltyhana.saltyhanaserver.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/goals")
    public ResponseEntity<List<CalendarResponseDTO>> getAllCalendarGoals() {
        List<CalendarResponseDTO> goals = calendarService.getAllCalendarGoals(111L);
        return ResponseEntity.ok(goals);
    }
}
