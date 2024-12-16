package com.saltyhana.saltyhanaserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


public class WrongPasswordException extends ResponseStatusException {
    public WrongPasswordException() {
        super(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다.");
    }
}
