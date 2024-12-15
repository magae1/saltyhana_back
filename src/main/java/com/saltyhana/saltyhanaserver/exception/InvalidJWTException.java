package com.saltyhana.saltyhanaserver.exception;


public class InvalidJWTException extends RuntimeException {
    public InvalidJWTException(String message) {
        super(message);
    }
}
