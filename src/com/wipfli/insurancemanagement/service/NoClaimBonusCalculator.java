package com.wipfli.insurancemanagement.service;

import com.wipfli.insurancemanagement.model.Policy;

public class NoClaimBonusCalculator implements PremiumCalculable {

    private static final double NO_CLAIM_DISCOUNT = 0.10;

    private final PremiumCalculable delegate;

    public NoClaimBonusCalculator(PremiumCalculable delegate) {
        this.delegate = delegate;
    }

    @Override
    public double calculatePremium(Policy policy) {

        if (policy == null) {
            throw new IllegalArgumentException("Policy cannot be null.");
        }

        double premium = delegate.calculatePremium(policy);

        if (hasNoClaims(policy)) {
            premium -= premium * NO_CLAIM_DISCOUNT;
        }

        return premium;
    }

    private boolean hasNoClaims(Policy policy) {
        return policy.getClaimCount() == 0;
    }
}