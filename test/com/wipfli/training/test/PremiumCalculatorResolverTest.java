package com.wipfli.training.test;

import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.CarPolicy;
import com.wipfli.training.model.Policy;
import com.wipfli.training.model.VehicleType;
import com.wipfli.training.repository.PolicyRepository;
import com.wipfli.training.service.NoClaimBonusCalculator;
import com.wipfli.training.service.PolicyRegister;
import com.wipfli.training.service.PremiumCalculable;
import com.wipfli.training.service.PremiumCalculatorResolver;
import com.wipfli.training.service.StandardPremiumCalculator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static com.wipfli.training.test.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PremiumCalculatorResolverTest {

    private final PremiumCalculatorResolver resolver =
            new PremiumCalculatorResolver(new StandardPremiumCalculator(), new NoClaimBonusCalculator(new StandardPremiumCalculator()));

    @Test
    void zeroClaimsSelectsNoClaimBonusCalculator() {
        Policy policy = CAR_POLICY_YOUNG_DRIVER_NO_CLAIMS;
        PremiumCalculable selected = resolver.resolve(policy);

        assertTrue(selected instanceof NoClaimBonusCalculator);
    }

    @Test
    void oneOrMoreClaimsSelectStandardPremiumCalculator() {
        assertTrue(resolver.resolve(CAR_POLICY_YOUNG_DRIVER_WITH_CLAIM) instanceof StandardPremiumCalculator);
        assertTrue(resolver.resolve(CAR_POLICY_MULTIPLE_CLAIMS) instanceof StandardPremiumCalculator);
    }

    @Test
    void decoratorDelegatesToStandardCalculatorAndAppliesBonusExactlyOnce() {
        StandardPremiumCalculator standardCalculator = new StandardPremiumCalculator();
        NoClaimBonusCalculator bonusCalculator = new NoClaimBonusCalculator(standardCalculator);

        double standardPremium = standardCalculator.calculatePremium(CAR_POLICY_YOUNG_DRIVER_NO_CLAIMS);
        double bonusPremium = bonusCalculator.calculatePremium(CAR_POLICY_YOUNG_DRIVER_NO_CLAIMS);

        assertEquals(standardPremium * 0.90, bonusPremium, 0.001);
        assertEquals(standardPremium, standardCalculator.calculatePremium(CAR_POLICY_YOUNG_DRIVER_NO_CLAIMS), 0.001);
    }

    @Test
    void nullAndIncompletePolicyDataAreHandledExplicitly() {
        assertThrows(IllegalArgumentException.class, () -> resolver.resolve(null));

        Policy incompletePolicy = new CarPolicy(
                "POL-9999",
                null,
                VehicleType.CAR,
                0,
                LocalDate.of(2027, 1, 1),
                1,
                "MH12AB9999");

        assertThrows(IllegalArgumentException.class, () -> resolver.resolve(incompletePolicy));
    }

    @Test
    void missingPolicyNumberProducesExpectedApplicationException() {
        PolicyRepository repository = mock(PolicyRepository.class);
        when(repository.findById("POL-404")).thenReturn(Optional.empty());

        PolicyRegister register = new PolicyRegister(resolver, repository);
        assertThrows(PolicyNotFoundException.class, () -> register.calculatePremium("POL-404"));
    }

    @Test
    void policyRegisterUsesCalculatorReturnedByResolver() throws PolicyNotFoundException {
        PolicyRepository repository = mock(PolicyRepository.class);
        when(repository.findById(CAR_POLICY_YOUNG_DRIVER_NO_CLAIMS.getPolicyNumber()))
                .thenReturn(Optional.of(CAR_POLICY_YOUNG_DRIVER_NO_CLAIMS));

        PolicyRegister register = new PolicyRegister(new PremiumCalculatorResolver(new StandardPremiumCalculator(), new NoClaimBonusCalculator(new StandardPremiumCalculator())) {
            @Override
            public PremiumCalculable resolve(Policy candidate) {
                return new PremiumCalculable() {
                    @Override
                    public double calculatePremium(Policy p) {
                        return 123.45;
                    }
                };
            }
        }, repository);

        assertEquals(123.45, register.calculatePremium(CAR_POLICY_YOUNG_DRIVER_NO_CLAIMS.getPolicyNumber()), 0.001);
    }
}

