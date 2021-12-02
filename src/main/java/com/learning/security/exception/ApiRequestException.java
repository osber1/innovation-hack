package com.learning.security.exception;

import org.springframework.http.HttpStatus;

public abstract class ApiRequestException extends RuntimeException {
    public ApiRequestException(String message) {
        super(message);
    }

    public abstract HttpStatus getHttpStatus();
}
