package com.learning.security.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApiException {
    private final LocalDateTime timestamp;
    private final HttpStatus status;
    private final String message;

    public ApiException(String message, LocalDateTime timestamp, HttpStatus status) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }
}
