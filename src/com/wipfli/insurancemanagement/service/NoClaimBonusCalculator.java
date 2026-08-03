package com.wipfli.insurancemanagement.service;

import com.wipfli.insurancemanagement.model.Policy;

public class NoClaimBonusCalculator implements PremiumCalculable {

    private static final int YOUNG_DRIVER_AGE_LIMIT = 25;
    private static final double YOUNG_DRIVER_LOADING_RATE = 0.20;
    private static final double REGULAR_DRIVER_LOADING_RATE = 0.15;
    private static final int CLAIM_PREMIUM_INCREASE = 150;
    private static final double NO_CLAIM_DISCOUNT = 0.10;

    @Override
    public double calculatePremium(Policy policy) {

        if (policy == null) {
            throw new IllegalArgumentException("Policy cannot be null.");
        }

        double basePremium = policy.getVehicleType().getBasePremium();

        double premium = basePremium;


        premium += calculateAgeLoading(policy.getPolicyOwner().getAge(), basePremium);

        premium += calculateClaimLoading(policy.getClaimCount());

        if (hasNoClaims(policy)) {
            premium -= premium *
                    NO_CLAIM_DISCOUNT;
        }

        return premium;
    }
    private double calculateAgeLoading(int age, double basePremium) {

        if (isYoungDriver(age)) {
            return basePremium * YOUNG_DRIVER_LOADING_RATE;
        }

        return basePremium * REGULAR_DRIVER_LOADING_RATE;
    }

    private boolean isYoungDriver(int age) {
        return age < YOUNG_DRIVER_AGE_LIMIT;
    }

    private double calculateClaimLoading(int claimCount) {
        return claimCount * CLAIM_PREMIUM_INCREASE;
    }

    private boolean hasNoClaims(Policy policy) {
        return policy.getClaimCount() == 0;
    }
}