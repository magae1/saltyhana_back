package com.saltyhana.saltyhanaserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GoogleCalendarException extends ResponseStatusException {

    public GoogleCalendarException(HttpStatus status, String reason) {
        super(status, reason);
    }

    //401
    public static class AuthenticationException extends GoogleCalendarException {
        public AuthenticationException() {
            super(HttpStatus.UNAUTHORIZED, "Google Calendar 인증이 필요합니다.");
        }
    }

    //400
    public static class InvalidAuthorizationCodeException extends GoogleCalendarException {
        public InvalidAuthorizationCodeException() {
            super(HttpStatus.BAD_REQUEST, "유효하지 않은 인증 코드입니다.");
        }
    }

    //500
    public static class InternalServerErrorException extends GoogleCalendarException {
        public InternalServerErrorException() {
            super(HttpStatus.INTERNAL_SERVER_ERROR, "캘린더 이벤트 불러오기 실패");
        }
    }
}
