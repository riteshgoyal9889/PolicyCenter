package com.wipfli.training.service;

import com.wipfli.training.model.Policy;
import org.springframework.stereotype.Component;

@Component("noClaimBonusCalculator")
public class NoClaimBonusCalculator implements PremiumCalculable {

    private static final double NO_CLAIM_DISCOUNT = 0.10;

    private final StandardPremiumCalculator delegate;

    public NoClaimBonusCalculator(StandardPremiumCalculator delegate) {
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
