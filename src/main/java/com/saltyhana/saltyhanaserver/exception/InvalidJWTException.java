package com.saltyhana.saltyhanaserver.exception;


public class InvalidJWTException extends RuntimeException {
    public InvalidJWTException(String message) {
        super(message);
    }

    public InvalidJWTException() {
        super("유효하지 않은 토큰입니다.");
    }
}
