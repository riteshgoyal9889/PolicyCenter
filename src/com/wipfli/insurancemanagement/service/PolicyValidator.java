package com.wipfli.insurancemanagement.service;

import com.wipfli.insurancemanagement.exception.InvalidPolicyDataException;
import com.wipfli.insurancemanagement.model.Policy;

public class PolicyValidator {

    private static final int MINIMUM_AGE = 18;

    public void validate(Policy policy) {

        if (policy == null) {
            throw new InvalidPolicyDataException("Policy cannot be null.");
        }

        if (policy.getPolicyOwner().getAge() < MINIMUM_AGE) {
            throw new InvalidPolicyDataException("Customer must be at least "+MINIMUM_AGE+" years old.");
        }

        if (policy.getClaimCount() < 0) {
            throw new InvalidPolicyDataException("Claim count cannot be negative.");
        }

        if (policy.getVehicleType() == null) {
            throw new InvalidPolicyDataException("Vehicle type cannot be empty.");
        }

        if (policy.getDurationInYears() <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0.");
        }
    }
}