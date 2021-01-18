package com.de.hiking.utils;

import org.springframework.http.HttpStatus;

/**
 * Custom exception handler which allows to display specific exception messages related to the Hiking API
 */
public class ExceptionResponse {

    private String errorCode;
    private String errorMessage;
    private HttpStatus httpStatus;

    public ExceptionResponse() {
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
