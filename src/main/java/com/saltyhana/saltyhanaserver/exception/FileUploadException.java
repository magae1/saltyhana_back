package com.saltyhana.saltyhanaserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FileUploadException extends ResponseStatusException {

    public FileUploadException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 중 오류가 발생했습니다.");
    }

    public FileUploadException(String reason) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, reason);
    }
}