package com.insurancemanagement.service;

import com.insurancemanagement.model.Policy;

public class PremiumCalculator implements PremiumCalculable {

    @Override
    public double calculatePremium(Policy policy) {

        if (policy == null) {
            throw new IllegalArgumentException("Policy cannot be null.");
        }

        double base = policy.getVehicleType().getBasePremium();

        double premium = base;

        if (policy.getCustomer().getAge() < 25) {
            premium += base * 0.20;
        }
        else{
            premium += base * 0.15;
        }

        premium += policy.getClaimCount() * 150;

        return premium;
    }
}