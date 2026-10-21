package com.wipfli.insurancemanagement.service;

import com.wipfli.insurancemanagement.exception.InvalidPolicyDataException;

import java.time.LocalDate;

public class PolicyValidator {

    private static final int MINIMUM_AGE = 18;

    public void validateCustomerName(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new InvalidPolicyDataException("UNKNOWN", "Customer name cannot be empty.");
        }

        for (char ch : name.toCharArray()) {
            if (!Character.isLetter(ch) && ch != ' ') {
                throw new InvalidPolicyDataException("UNKNOWN", "Name must contain only letters and spaces.");
            }
        }
    }
    public void validateCustomerAge(int age) {
        if (age < MINIMUM_AGE || age > 100) {
            throw new InvalidPolicyDataException("UNKNOWN", "Age must be between 18 and 100.");
        }
    }

    public void validatePolicyNumber(String policyNumber) {
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            throw new InvalidPolicyDataException("UNKNOWN", "Policy number cannot be empty.");
        }
    }

    public void validateClaimCount(String policyNumber, int claimCount) {
        if (claimCount < 0) {
            throw new InvalidPolicyDataException(policyNumber, "Claim count cannot be negative.");
        }
    }
    public void validateExpiryDate(String policyNumber, LocalDate startDate, int durationInYears) {
        LocalDate expiryDate = startDate.plusYears(durationInYears);

        if (expiryDate.isBefore(LocalDate.now())) {
            throw new InvalidPolicyDataException(policyNumber, "Policy expiry date cannot be in the past.");
        }
    }

}