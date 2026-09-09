package com.wipfli.insurancemanagement.test;

import com.wipfli.insurancemanagement.model.Policy;
import com.wipfli.insurancemanagement.service.NoClaimBonusCalculator;
import com.wipfli.insurancemanagement.service.StandardPremiumCalculator;
import org.junit.jupiter.api.Test;
import static com.wipfli.insurancemanagement.test.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NoClaimBonusCalculatorTest {

    private final NoClaimBonusCalculator bonusCalculator = new NoClaimBonusCalculator((new StandardPremiumCalculator()));
    private final StandardPremiumCalculator standardCalculator = new StandardPremiumCalculator();

/*

1.No-claim bonus increases premium for a young driver with no claims
2.No-claim bonus is applied for a truck policy with no claims
3.No-claim bonus is applied for a bike policy with no claims
4.No-claim bonus is not applied when claims exist
5.Bonus premium is always less than or equal to standard premium
 */

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

