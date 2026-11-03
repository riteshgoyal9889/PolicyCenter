package com.wipfli.training.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PolicyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePolicyNotFound(PolicyNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({DuplicatePolicyNumberException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicatePolicyNumber(DuplicatePolicyNumberException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler({InvalidPolicyDataException.class, IllegalStatusChangeException.class, RenewalNotAllowedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(PolicyBusinessException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex) {
        return ex.getMessage();
    }
}
