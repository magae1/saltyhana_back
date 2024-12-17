package com.saltyhana.saltyhanaserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AlreadyExistsException extends ResponseStatusException {
    public AlreadyExistsException(String targetName) {
        super(HttpStatus.BAD_REQUEST, String.format("이미 존재하는 %s입니다.", targetName));
    }
}
