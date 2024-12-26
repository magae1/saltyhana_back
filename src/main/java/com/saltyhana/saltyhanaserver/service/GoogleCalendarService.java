package com.saltyhana.saltyhanaserver.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoogleCalendarService {

    private final GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow;
    private final ObjectMapper objectMapper;
    private Credential credential;

    @Value("${google.calendar.redirect-uris}")
    private String redirectUri;

    @Autowired
    public GoogleCalendarService(GoogleAuthorizationCodeFlow googleAuthorizationCodeFlow, ObjectMapper objectMapper) {
        this.googleAuthorizationCodeFlow = googleAuthorizationCodeFlow;
        this.objectMapper = objectMapper;
    }

    public String getAuthUrl() throws Exception {
        return googleAuthorizationCodeFlow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .build();
    }

    public Credential exchangeCodeForCredential(String code) throws Exception {
        GoogleTokenResponse response = googleAuthorizationCodeFlow.newTokenRequest(code)
                .setRedirectUri(redirectUri)
                .execute();
        Credential userCredential = googleAuthorizationCodeFlow.createAndStoreCredential(response, "user");
        this.credential = userCredential;
        return userCredential;
    }

    private void ensureCredentialIsSet() {
        if (credential == null) {
            throw new IllegalStateException("Credential is not set. Please authenticate first.");
        }
    }

    public String getGoogleCalendarEvents() throws Exception {
        ensureCredentialIsSet();
        Calendar service = new Calendar.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                .setApplicationName("Saltyhana Calendar Integration")
                .build();

        //현재 시점부터 1년 전까지의 스케줄 로딩
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime oneYearAgo = now.minusYears(1);

        String timeMin = oneYearAgo.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Events events = service.events().list("primary")
                .setTimeMin(new com.google.api.client.util.DateTime(timeMin))
                .execute();

        List<Map<String, Object>> formattedEvents = new ArrayList<>();

        for (Event event : events.getItems()) {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("id", event.getId());
            eventData.put("summary", event.getSummary());
            eventData.put("start", event.getStart().getDateTime() != null ? event.getStart().getDateTime().toStringRfc3339() : event.getStart().getDate().toString());
            eventData.put("htmlLink", event.getHtmlLink());
            formattedEvents.add(eventData);
        }
        return objectMapper.writeValueAsString(formattedEvents);
    }

    public String addEvent(Map<String, Object> eventData) throws Exception {
        ensureCredentialIsSet();

        Calendar service = new Calendar.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance(), credential)
                .setApplicationName("Saltyhana Calendar Integration")
                .build();

        String bankName = (String) eventData.get("bankName");
        int year = (int) eventData.get("year");
        int month = (int) eventData.get("month");
        int day = (int) eventData.get("day");
        String time = (String) eventData.get("time");

        if (bankName == null || time == null) {
            throw new IllegalArgumentException("bankName 및 time 정보를 모두 제공해야 합니다.");
        }

        // 초를 포함한 형식으로 변환
        String formattedDate = String.format("%04d-%02d-%02dT%s:00+09:00", year, month, day, time);
        ZonedDateTime startDateTime = ZonedDateTime.parse(formattedDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        ZonedDateTime endDateTime = startDateTime.plusMinutes(30);

        String formattedStartDateTime = startDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String formattedEndDateTime = endDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        Event event = new Event()
                .setSummary(bankName+" 방문 예약")
                .setLocation(bankName);

        EventDateTime start = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(formattedStartDateTime))
                .setTimeZone("Asia/Seoul");
        EventDateTime end = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(formattedEndDateTime))
                .setTimeZone("Asia/Seoul");

        event.setStart(start).setEnd(end);

        Event createdEvent = service.events().insert("primary", event).execute();

        Map<String, Object> response = new HashMap<>();
        response.put("id", createdEvent.getId());
        response.put("summary", createdEvent.getSummary());
        response.put("htmlLink", createdEvent.getHtmlLink());

        return objectMapper.writeValueAsString(response);
    }
}