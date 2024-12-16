package com.saltyhana.saltyhanaserver.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {
    public NotFoundException(String targetName) {
        super(HttpStatus.NOT_FOUND, targetName + "을(를) 찾을 수 없습니다.");
    }
}
