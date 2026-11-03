package com.wipfli.training.service;

import com.wipfli.training.model.Policy;
import org.springframework.stereotype.Component;

@Component
public class PremiumCalculatorResolver {

    private final StandardPremiumCalculator standardPremiumCalculator;
    private final NoClaimBonusCalculator noClaimBonusCalculator;

    public PremiumCalculatorResolver(
            StandardPremiumCalculator standardPremiumCalculator,
            NoClaimBonusCalculator noClaimBonusCalculator) {
        this.standardPremiumCalculator = standardPremiumCalculator;
        this.noClaimBonusCalculator = noClaimBonusCalculator;
    }

    public PremiumCalculable resolve(Policy policy) {
        validatePolicy(policy);

        if (policy.getClaimCount() == 0) {
            return noClaimBonusCalculator;
        }

        return standardPremiumCalculator;
    }

    private void validatePolicy(Policy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Policy cannot be null.");
        }
        if (policy.getPolicyHolder() == null) {
            throw new IllegalArgumentException("Policy owner cannot be null.");
        }
        if (policy.getVehicleType() == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null.");
        }
        if (policy.getClaimCount() < 0) {
            throw new IllegalArgumentException("Claim count cannot be negative.");
        }
    }
}
