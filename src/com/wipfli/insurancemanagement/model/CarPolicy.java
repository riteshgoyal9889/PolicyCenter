package com.wipfli.insurancemanagement.model;

import java.time.LocalDate;

public class CarPolicy extends Policy {

    private final String registrationNumber;

    public CarPolicy(String policyNumber, PolicyOwner policyOwner, VehicleType vehicleType, int claimCount, LocalDate startDate, int durationInYears, String registrationNumber) {

        super(policyNumber, policyOwner, vehicleType, claimCount, startDate, durationInYears);

        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @Override
    public String getPolicyDetails() {

        return "Policy Number: " + getPolicyNumber()
                + ", Owner: " + getPolicyOwner().getName() + "," +
                " Status: " + getStatus() + "," +
                " Registration Number: " + registrationNumber +
                "claim count " + getClaimCount();
    }
}