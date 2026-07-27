package com.wipfli.insurancemanagement.exception;

public abstract class PolicyBusinessException
        extends RuntimeException {

    private final String policyNumber;

    public PolicyBusinessException(
            String policyNumber,
            String message) {

        super(message);
        this.policyNumber = policyNumber;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }
}