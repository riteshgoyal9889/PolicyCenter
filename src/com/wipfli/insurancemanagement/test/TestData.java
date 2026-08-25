package com.wipfli.insurancemanagement.test;

import com.wipfli.insurancemanagement.model.BikePolicy;
import com.wipfli.insurancemanagement.model.CarPolicy;
import com.wipfli.insurancemanagement.model.Policy;
import com.wipfli.insurancemanagement.model.PolicyOwner;
import com.wipfli.insurancemanagement.model.TruckPolicy;
import com.wipfli.insurancemanagement.model.VehicleType;

import java.time.LocalDate;
import java.util.List;

public final class TestData {

    public static final LocalDate REFERENCE_DATE = LocalDate.of(2026, 8, 9);

    private static final PolicyOwner RAVI = new PolicyOwner("Ravi", 22);
    private static final PolicyOwner MEENA = new PolicyOwner("Meena", 34);
    private static final PolicyOwner AJAY = new PolicyOwner("Ajay", 41);
    private static final PolicyOwner SNEHA = new PolicyOwner("Sneha", 29);

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

    public static List<Policy> sixPolicyDataset() {
        return List.of(
                CAR_POLICY_YOUNG_DRIVER_WITH_CLAIM,
                TRUCK_POLICY_NO_CLAIMS,
                BIKE_POLICY_WITH_CLAIMS,
                CAR_POLICY_REGULAR_DRIVER_NO_CLAIM,
                BIKE_POLICY_NO_CLAIMS,
                CAR_POLICY_MULTIPLE_CLAIMS,
                CAR_POLICY_YOUNG_DRIVER_NO_CLAIMS
        );
    }
}

