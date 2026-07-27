package com.wipfli.insurancemanagement.exception;

public class InvalidPolicyDataException
        extends PolicyBusinessException {

    public InvalidPolicyDataException(
            String policyNumber,
            String message) {

        super(policyNumber, message);
    }
}