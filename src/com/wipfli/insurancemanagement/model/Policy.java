package com.wipfli.insurancemanagement.model;

import java.time.LocalDate;
import java.util.Objects;

public class Policy {

    private final String policyNumber;
    private final PolicyOwner policyOwner;
    private final VehicleType vehicleType;
    private final int claimCount;
    private final LocalDate startDate;
    private final double durationInYears;

    public Policy(String policyNumber, PolicyOwner policyOwner, VehicleType vehicleType, int claimCount, LocalDate startDate, double durationInYears) {

//        if (policyNumber == null || policyNumber.trim().isEmpty()) {
//            throw new IllegalArgumentException("Policy number cannot be empty.");
//        }
//
//        if (policyOwner == null) {
//            throw new IllegalArgumentException("Customer cannot be null.");
//        }
//
//        if (vehicleType == null) {
//            throw new IllegalArgumentException("Vehicle type cannot be null.");
//        }
//
//        if (claimCount < 0) {
//            throw new IllegalArgumentException("Claim count cannot be negative.");
//        }
//
//        if (startDate == null) {
//            throw new IllegalArgumentException("Start date cannot be null.");
//        }
//
//        if (durationInYears < 0) {
//            throw new IllegalArgumentException("Duration must be greater than 0.");
//        }

        this.policyNumber = policyNumber.trim();
        this.policyOwner = policyOwner;
        this.vehicleType = vehicleType;
        this.claimCount = claimCount;
        this.startDate = startDate;
        this.durationInYears = durationInYears;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public PolicyOwner getCustomer() {
        return policyOwner;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getClaimCount() {
        return claimCount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public double getDurationInYears() {
        return durationInYears;
    }
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Policy)) {
            return false;
        }

        Policy policy = (Policy) obj;
        return Objects.equals(policyNumber, policy.policyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policyNumber);
    }
}