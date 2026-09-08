package com.wipfli.insurancemanagement.test;

import com.wipfli.insurancemanagement.model.Policy;
import com.wipfli.insurancemanagement.service.StandardPremiumCalculator;
import org.junit.jupiter.api.Test;

import static com.wipfli.insurancemanagement.test.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StandardPremiumCalculatorTest {

    private final StandardPremiumCalculator calculator = new StandardPremiumCalculator();

    /*
1.Calculate premium for CAR policy with young driver and claim
2.Calculate premium for TRUCK policy with young driver and no claims
3.Calculate premium for BIKE policy with regular driver and claims
4.Calculate premium for CAR policy with regular driver and no claims
6.Calculate premium for CAR policy with multiple claims
*/

    @Test
    void calculatePremiumForCarWithYoungDriverAndClaim() {
        double premium = calculator.calculatePremium(CAR_POLICY_YOUNG_DRIVER_WITH_CLAIM);
        assertEquals(750.0, premium, 0.001);
    }

    @Test
    void calculatePremiumForTruckWithYoungDriverAndNoClaims() {
        double premium = calculator.calculatePremium(TRUCK_POLICY_NO_CLAIMS);
        assertEquals(960.0, premium, 0.001);
    }

    @Test
    void calculatePremiumForBikeWithRegularDriverAndClaims() {
        double premium = calculator.calculatePremium(BIKE_POLICY_WITH_CLAIMS);
        assertEquals(600.0, premium, 0.001);
    }

    @Test
    void calculatePremiumForCarWithRegularDriverAndNoClaims() {
        double premium = calculator.calculatePremium(CAR_POLICY_REGULAR_DRIVER_NO_CLAIM);
        assertEquals(500.0, premium, 0.001);
    }

    @Test
    void calculatePremiumForCarWithMultipleClaims() {
        double premium = calculator.calculatePremium(CAR_POLICY_MULTIPLE_CLAIMS);
        assertEquals(1050.0, premium, 0.001);
    }
}

