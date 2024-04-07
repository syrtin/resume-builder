package com.syrtin.dataprocessor.exception;

import org.springframework.http.HttpStatus;

public class PhotoUploadException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public PhotoUploadException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
