package com.insurancemanagement.service;

import com.insurancemanagement.model.Policy;

public class PolicyValidator {

    private static final int MINIMUM_AGE = 18;

    public void validate(Policy policy) {

        if (policy == null) {
            throw new IllegalArgumentException("Policy cannot be null.");
        }

        if (policy.getCustomer().getAge() < MINIMUM_AGE) {
            throw new IllegalArgumentException("Customer must be at least "+MINIMUM_AGE+" years old.");
        }

        if (policy.getClaimCount() < 0) {
            throw new IllegalArgumentException("Claim count cannot be negative.");
        }

        if (policy.getVehicleType() == null) {
            throw new IllegalArgumentException("Vehicle type cannot be empty.");
        }

        if (policy.getDurationInYears() <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0.");
        }
    }
}