package com.de.hiking.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Allows to catch the <code>ResponseStatusExceptions</code> and fill them with a custom error message
 */
@ControllerAdvice
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> resourceNotFound(ResponseStatusException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorMessage(ex.getMessage());
        response.setHttpStatus(ex.getStatus());
        response.setErrorCode(ex.getReason());
        return new ResponseEntity<>(response, ex.getStatus());
    }
}