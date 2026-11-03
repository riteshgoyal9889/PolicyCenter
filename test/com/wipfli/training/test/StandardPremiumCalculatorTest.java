package com.wipfli.training.test;

import com.wipfli.training.service.StandardPremiumCalculator;
import org.junit.jupiter.api.Test;

import static com.wipfli.training.test.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StandardPremiumCalculatorTest {

    private final StandardPremiumCalculator calculator = new StandardPremiumCalculator();

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

