package com.learning.security.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                LocalDateTime.now(),
                e.getHttpStatus());

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException e) {
        ApiException apiException = new ApiException(
                e.getMessage(),
                LocalDateTime.now(),
                e.getHttpStatus());

        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}
