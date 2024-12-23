package com.saltyhana.saltyhanaserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {
  
  public BadRequestException() {
    super(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
  }
}
