package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.service.GoogleCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api_prefix}/google-calendar")
public class GoogleCalendarController {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @GetMapping("/auth")
    public ResponseEntity<String> getAuthUrl() throws Exception {
        String authUrl = googleCalendarService.getAuthUrl();
        return ResponseEntity.ok(authUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> getCalendarEvents(@RequestParam String code) throws Exception {
        googleCalendarService.exchangeCodeForCredential(code);
        String events = googleCalendarService.getGoogleCalendarEvents();
        return ResponseEntity.ok(events);
    }

    @PostMapping("/insert")
    public ResponseEntity<String> addCalendarEvents(@RequestBody Map<String, Object> eventData) {
        try {
            googleCalendarService.addEvent(eventData);
            return ResponseEntity.ok("Event successfully reserved.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding event: " + e.getMessage());
        }
    }

}
