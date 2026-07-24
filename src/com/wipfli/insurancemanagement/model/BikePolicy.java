package com.wipfli.insurancemanagement.model;

import java.time.LocalDate;

public class BikePolicy extends Policy {

    private final int engineCC;

    public BikePolicy(String policyNumber,PolicyOwner policyOwner, VehicleType vehicleType, int claimCount, LocalDate startDate, int durationInYears, int engineCC) {

        super(policyNumber, policyOwner, vehicleType, claimCount, startDate, durationInYears);

        this.engineCC = engineCC;
    }


    @Override
    public String getPolicyDetails() {

        return "Policy Number: "
                + getPolicyNumber()
                + ", Owner: "
                + getPolicyOwner().getName()
                + ", Status: "
                + getStatus()
                + ", Engine CC: "
                + engineCC;
    }
}