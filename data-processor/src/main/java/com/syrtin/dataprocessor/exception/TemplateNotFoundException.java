package com.syrtin.dataprocessor.exception;

import org.springframework.http.HttpStatus;

public class TemplateNotFoundException extends RuntimeException {

    private final HttpStatus status;

    private final String message;

    public TemplateNotFoundException(HttpStatus status, String message) {
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
