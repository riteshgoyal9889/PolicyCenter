package com.wipfli.training.test;

import com.wipfli.training.model.Policy;
import com.wipfli.training.service.NoClaimBonusCalculator;
import com.wipfli.training.service.StandardPremiumCalculator;
import org.junit.jupiter.api.Test;

import static com.wipfli.training.test.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NoClaimBonusCalculatorTest {

    private final NoClaimBonusCalculator bonusCalculator = new NoClaimBonusCalculator(new StandardPremiumCalculator());
    private final StandardPremiumCalculator standardCalculator = new StandardPremiumCalculator();

    @Test
    void noClaimBonusIncreasesPremiumForYoungDriver() {
        Policy policy = CAR_POLICY_YOUNG_DRIVER_NO_CLAIMS;
        double standardPremium = standardCalculator.calculatePremium(policy);
        double bonusPremium = bonusCalculator.calculatePremium(policy);
        assertEquals(standardPremium * 0.90, bonusPremium, 0.001);
    }

    @Test
    void noClaimBonusIsAppliedForTruckWithNoClaims() {
        Policy policy = TRUCK_POLICY_NO_CLAIMS;
        double standardPremium = standardCalculator.calculatePremium(policy);
        double bonusPremium = bonusCalculator.calculatePremium(policy);
        assertEquals(standardPremium * 0.90, bonusPremium, 0.001);
    }

    @Test
    void noClaimBonusIsAppliedForBikeWithNoClaims() {
        Policy policy = BIKE_POLICY_NO_CLAIMS;
        double standardPremium = standardCalculator.calculatePremium(policy);
        double bonusPremium = bonusCalculator.calculatePremium(policy);
        assertEquals(standardPremium * 0.90, bonusPremium, 0.001);
    }

    @Test
    void noClaimBonusIsNotAppliedWhenClaimsExist() {
        Policy policy = CAR_POLICY_MULTIPLE_CLAIMS;
        double standardPremium = standardCalculator.calculatePremium(policy);
        double bonusPremium = bonusCalculator.calculatePremium(policy);
        assertEquals(standardPremium, bonusPremium, 0.001);
    }

    @Test
    void bonusPremiumIsAlwaysLessThanOrEqualToStandardPremium() {
        Policy policy = CAR_POLICY_YOUNG_DRIVER_WITH_CLAIM;
        double standardPremium = standardCalculator.calculatePremium(policy);
        double bonusPremium = bonusCalculator.calculatePremium(policy);
        assertTrue(bonusPremium <= standardPremium);
    }
}

