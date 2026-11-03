package com.wipfli.training.test;

import com.wipfli.training.model.BikePolicy;
import com.wipfli.training.model.CarPolicy;
import com.wipfli.training.model.Policy;
import com.wipfli.training.model.PolicyHolder;
import com.wipfli.training.model.TruckPolicy;
import com.wipfli.training.model.VehicleType;

import java.time.LocalDate;
import java.util.List;

public final class TestData {

    public static final LocalDate REFERENCE_DATE = LocalDate.of(2026, 8, 9);

    private static final PolicyHolder RAVI = new PolicyHolder("Ravi", 22);
    private static final PolicyHolder MEENA = new PolicyHolder("Meena", 34);
    private static final PolicyHolder AJAY = new PolicyHolder("Ajay", 41);
    private static final PolicyHolder SNEHA = new PolicyHolder("Sneha", 29);

    public static final Policy CAR_POLICY_YOUNG_DRIVER_WITH_CLAIM =
            new CarPolicy("POL-2001", RAVI, VehicleType.CAR, 1, LocalDate.of(2026, 8, 15), 1, "MH12AB9889");

    public static final Policy TRUCK_POLICY_NO_CLAIMS =
            new TruckPolicy("POL-2002", RAVI, VehicleType.TRUCK, 0, LocalDate.of(2026, 9, 1), 1, 5000);

    public static final Policy BIKE_POLICY_WITH_CLAIMS =
            new BikePolicy("POL-2003", MEENA, VehicleType.BIKE, 2, LocalDate.of(2026, 8, 20), 1, 500);

    public static final Policy CAR_POLICY_REGULAR_DRIVER_NO_CLAIM =
            new CarPolicy("POL-2004", AJAY, VehicleType.CAR, 0, LocalDate.of(2027, 1, 10), 1, "MH12AB7887");

    public static final Policy BIKE_POLICY_NO_CLAIMS =
            new BikePolicy("POL-2005", SNEHA, VehicleType.BIKE, 0, LocalDate.of(2026, 8, 25), 1, 200);

    public static final Policy CAR_POLICY_MULTIPLE_CLAIMS =
            new CarPolicy("POL-2006", RAVI, VehicleType.CAR, 3, LocalDate.of(2026, 12, 1), 1, "MH12AB1235");

    public static final Policy CAR_POLICY_YOUNG_DRIVER_NO_CLAIMS =
            new CarPolicy("POL-2007", RAVI, VehicleType.CAR, 0, LocalDate.of(2026, 9, 15), 1, "MH12AB1234");

    private TestData() {
    }

    public static Policy freshCarPolicyYoungDriverWithClaim() {
        return new CarPolicy("POL-2001", new PolicyHolder("RAVI-22-A", "Ravi", 22), VehicleType.CAR, 1, LocalDate.of(2026, 8, 15), 1, "MH12AB9889");
    }

    public static Policy freshTruckPolicyNoClaims() {
        return new TruckPolicy("POL-2002", new PolicyHolder("RAVI-22-B", "Ravi", 22), VehicleType.TRUCK, 0, LocalDate.of(2026, 9, 1), 1, 5000);
    }

    public static Policy freshBikePolicyWithClaims() {
        return new BikePolicy("POL-2003", new PolicyHolder("MEENA-34", "Meena", 34), VehicleType.BIKE, 2, LocalDate.of(2026, 8, 20), 1, 500);
    }

    public static Policy freshCarPolicyRegularDriverNoClaim() {
        return new CarPolicy("POL-2004", new PolicyHolder("AJAY-41", "Ajay", 41), VehicleType.CAR, 0, LocalDate.of(2027, 1, 10), 1, "MH12AB7887");
    }

    public static Policy freshBikePolicyNoClaims() {
        return new BikePolicy("POL-2005", new PolicyHolder("SNEHA-29", "Sneha", 29), VehicleType.BIKE, 0, LocalDate.of(2026, 8, 25), 1, 200);
    }

    public static Policy freshCarPolicyMultipleClaims() {
        return new CarPolicy("POL-2006", new PolicyHolder("RAVI-22-C", "Ravi", 22), VehicleType.CAR, 3, LocalDate.of(2026, 12, 1), 1, "MH12AB1235");
    }

    public static Policy freshCarPolicyYoungDriverNoClaims() {
        return new CarPolicy("POL-2007", new PolicyHolder("RAVI-22-D", "Ravi", 22), VehicleType.CAR, 0, LocalDate.of(2026, 9, 15), 1, "MH12AB1234");
    }

    public static List<Policy> sixPolicyDataset() {
        return List.of(
                freshCarPolicyYoungDriverWithClaim(),
                freshTruckPolicyNoClaims(),
                freshBikePolicyWithClaims(),
                freshCarPolicyRegularDriverNoClaim(),
                freshBikePolicyNoClaims(),
                freshCarPolicyMultipleClaims(),
                freshCarPolicyYoungDriverNoClaims()
        );
    }
}

