import com.wipfli.training.dto.APIStatus;
import com.wipfli.training.dto.PolicyResponse;
import com.wipfli.training.model.CarPolicy;
import com.wipfli.training.model.Policy;
import com.wipfli.training.model.PolicyOwner;
import com.wipfli.training.model.VehicleType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PolicyResponseTest {

    @Test
    void policyResponseIncludesApiStatusMetadata() {
        PolicyOwner owner = new PolicyOwner("Ravi", 29);
        Policy policy = new CarPolicy(
                "CAR1001",
                owner,
                VehicleType.CAR,
                1,
                LocalDate.of(2027, 1, 1),
                1,
                "MH12AB1234"
        );

        PolicyResponse response = PolicyResponse.from(policy, HttpStatus.CREATED, "Policy created successfully");

        assertNotNull(response.getApiStatus());
        assertEquals(201, response.getApiStatus().getStatusCode());
        assertEquals(true, response.getApiStatus().isSuccess());
        assertEquals("Policy created successfully", response.getApiStatus().getMessage());
        assertEquals("CAR1001", response.getPolicyNumber());
    }

    @Test
    void apiStatusFactoryBuildsExpectedMetadata() {
        APIStatus status = APIStatus.of(HttpStatus.OK, "Policy retrieved successfully");

        assertEquals(200, status.getStatusCode());
        assertEquals(true, status.isSuccess());
        assertEquals("Policy retrieved successfully", status.getMessage());
    }
}

