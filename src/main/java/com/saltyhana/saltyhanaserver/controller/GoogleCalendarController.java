package com.saltyhana.saltyhanaserver.controller;

import com.saltyhana.saltyhanaserver.exception.GoogleCalendarException;
import com.saltyhana.saltyhanaserver.service.GoogleCalendarService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("${api_prefix}/google-calendar")
public class GoogleCalendarController {

    @Autowired
    private GoogleCalendarService googleCalendarService;

    @Operation(summary = "oauth 인증 주소")
    @GetMapping("/auth")
    public ResponseEntity<String> getAuthUrl() throws ResponseStatusException {
        String authUrl = googleCalendarService.getAuthUrl();
        return ResponseEntity.ok(authUrl);
    }

    @Operation(summary = "google oauth 콜백 url")
    @GetMapping("/callback")
    public void getCalendarEvents(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws IOException {
        try {
            googleCalendarService.exchangeCodeForCredential(code, state);

            // 인증 성공 후 팝업 창 닫기를 위한 HTML 응답
            response.setContentType("text/html");
            response.getWriter().write("<script>window.opener.postMessage('AUTH_SUCCESS', '*'); window.close();</script>");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/html");
            response.getWriter().write("<script>window.opener.postMessage('AUTH_FAILED', '*'); window.close();</script>");
        }
    }

    @Operation(summary = "구글 캘린더 이벤트 조회")
    @GetMapping("/events")
    public ResponseEntity<String> getEvents() throws ResponseStatusException {
        try {
            String events = googleCalendarService.getGoogleCalendarEvents();
            return ResponseEntity.ok(events);
        } catch (GoogleCalendarException.AuthenticationException e) {
            log.warn("Authentication required for calendar access");
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch events: {}", e.getMessage(), e);
            throw new GoogleCalendarException.InternalServerErrorException();
        }
    }

    @Operation(summary = "새 이벤트 추가")
    @PostMapping("/insert")
    public ResponseEntity<String> addCalendarEvents(@RequestBody Map<String, Object> eventData) throws ResponseStatusException {
        try {
            googleCalendarService.addEvent(eventData);
            return ResponseEntity.ok("Event successfully reserved.");
        } catch (Exception e) {
            throw new GoogleCalendarException.InternalServerErrorException();
        }
    }
}
