package com.wipfli.insurancemanagement.exception;

public class RenewalNotAllowedException extends PolicyBusinessException {

    public RenewalNotAllowedException(String policyNumber, String message) {

        super(policyNumber, message);
    }
}