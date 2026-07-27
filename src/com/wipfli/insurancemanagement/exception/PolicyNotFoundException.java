package com.wipfli.insurancemanagement.exception;

public class PolicyNotFoundException
        extends Exception {

    public PolicyNotFoundException(
            String message) {

        super(message);
    }
}