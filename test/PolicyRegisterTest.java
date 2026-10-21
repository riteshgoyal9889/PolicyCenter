import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.model.Policy;
import com.wipfli.training.model.VehicleType;
import com.wipfli.training.repository.InMemoryPolicyRepository;
import com.wipfli.training.repository.PolicyRepository;
import com.wipfli.training.service.PolicyRegister;
import com.wipfli.training.service.PremiumCalculatorResolver;
import com.wipfli.training.service.StandardPremiumCalculator;
import com.wipfli.training.service.NoClaimBonusCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static TestData.CAR_POLICY_YOUNG_DRIVER_WITH_CLAIM;
import static TestData.REFERENCE_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PolicyRegisterTest {

    /*
    1.Find policy by number returns matching policy
    2.Find policy by number returns empty when policy does not exist
    3.Count policies by vehicle type groups correctly
    4.Find policies expiring within date range
    5.Find policies by customer name
    6.find  policy by vehicle type
    7.Find customer with most policies
    8.Duplicate policy number throws DuplicatePolicyNumberException
     */
    private PolicyRegister register;

    @BeforeEach
    void setUp() {
        PolicyRepository repository = new InMemoryPolicyRepository();
        PremiumCalculatorResolver resolver = new PremiumCalculatorResolver(
                new StandardPremiumCalculator(),
                new NoClaimBonusCalculator(new StandardPremiumCalculator())
        );

        register = new PolicyRegister(resolver, repository);
        TestData.sixPolicyDataset().forEach(register::add);
    }

    @Test
    void findByPolicyNumberReturnsPolicyWhenFound() {
        Optional<Policy> result = register.findByPolicyNumber("POL-2001");

        assertTrue(result.isPresent());
        assertEquals("POL-2001", result.get().getPolicyNumber());
    }

    @Test
    void findByPolicyNumberReturnsEmptyWhenNotFound() {
        Optional<Policy> result = register.findByPolicyNumber("POL-9999");
        assertTrue(result.isEmpty());
    }

    @Test
    void countPoliciesByVehicleTypeGroupsCorrectly() {
        Map<VehicleType, Long> counts = register.countPoliciesByVehicleType();

        assertEquals(4L, counts.get(VehicleType.CAR));
        assertEquals(2L, counts.get(VehicleType.BIKE));
        assertEquals(1L, counts.get(VehicleType.TRUCK));
    }

    @Test
    void findPoliciesExpiringWithinReturnsPoliciesInRange() {
        List<Policy> expiring = register.findPoliciesExpiringWithin(400,REFERENCE_DATE);

        assertEquals(4, expiring.size());
        assertEquals("POL-2001", expiring.get(0).getPolicyNumber());
        assertEquals("POL-2003", expiring.get(1).getPolicyNumber());
        assertEquals("POL-2005", expiring.get(2).getPolicyNumber());
        assertEquals("POL-2002", expiring.get(3).getPolicyNumber());
    }

    @Test
    void findPoliciesByCustomerNameReturnsMatchingPolicies() {
        List<Policy> policies = register.findPoliciesByCustomerName("Ravi");

        assertEquals(4, policies.size());

        assertTrue(
                policies.stream().allMatch(
                        policy -> policy.getPolicyOwner().getName().equalsIgnoreCase("Ravi")));
    }

    @Test
    void findByVehicleTypeReturnsMatchingPolicies() {
        List<Policy> policies = register.findByVehicleType(VehicleType.CAR);

        assertEquals(4, policies.size());
        assertTrue(policies.stream().allMatch(policy ->policy.getVehicleType() == VehicleType.CAR));
    }

    @Test
    void findCustomerWithMostPoliciesReturnsCorrectCustomer() {
        Optional<String> result = register.findCustomerWithMostPoliciesUsingStream();

        assertTrue(result.isPresent());
        assertEquals("Ravi", result.get());
    }

    @Test
    void addThrowsDuplicatePolicyNumberException() {
        assertThrows(DuplicatePolicyNumberException.class,
                () -> register.add(CAR_POLICY_YOUNG_DRIVER_WITH_CLAIM));
    }
}
