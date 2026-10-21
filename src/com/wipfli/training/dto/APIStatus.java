package com.wipfli.training.dto;

import org.springframework.http.HttpStatus;

public class APIStatus {

    private int statusCode;
    private boolean success;
    private String message;



    public APIStatus(int statusCode, boolean success, String message) {
        this.statusCode = statusCode;
        this.success = success;
        this.message = message;
    }

    public static APIStatus of(HttpStatus httpStatus, String message) {
        return new APIStatus(
                httpStatus.value(),
                httpStatus.is2xxSuccessful(),
                message
        );
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

