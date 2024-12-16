package com.saltyhana.saltyhanaserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundSampleException extends ResponseStatusException {
    public NotFoundSampleException(String reason) {
      super(HttpStatus.NOT_FOUND, reason);
    }
}
