package com.wipfli.insurancemanagement.model;

import com.wipfli.insurancemanagement.exception.InvalidPolicyDataException;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Policy {

    private final String policyNumber;
    private final PolicyOwner policyOwner;
    private final VehicleType vehicleType;
    private  int claimCount;
    private final LocalDate startDate;
    private final double durationInYears;
    private final LocalDate expiryDate;
    private PolicyStatus status;


    public Policy(String policyNumber, PolicyOwner policyOwner, VehicleType vehicleType, int claimCount, LocalDate startDate, double durationInYears) {
        this.policyNumber = policyNumber.trim();
        this.policyOwner = policyOwner;
        this.vehicleType = vehicleType;
        this.claimCount = claimCount;
        this.startDate = startDate;
        this.durationInYears = durationInYears;
        this.expiryDate =startDate.plusYears((Long)durationInYears);
        if (expiryDate.isBefore(LocalDate.now())) {
            throw new InvalidPolicyDataException(policyNumber, "Policy expiry date cannot be in the past.");
        }
        this.status=PolicyStatus.ACTIVE;

    }
    public Policy(String policyNumber, PolicyOwner policyOwner, VehicleType vehicleType, LocalDate startDate, int durationInYears) {

        this(policyNumber, policyOwner, vehicleType, 0, startDate, durationInYears);
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public PolicyOwner getPolicyOwner() {
        return policyOwner;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public int getClaimCount() {
        return claimCount;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public LocalDate getStartDate() {return startDate;}

    public double getDurationInYears() {return durationInYears;}

    public LocalDate getExpiryDate() {  return expiryDate;}

    public void recordClaim() { claimCount++;}

    public void expirePolicy() {
        isPolicyActive();
        status = PolicyStatus.EXPIRED;
    }

    public void renewPolicy() {
        isPolicyActive();
        status = PolicyStatus.RENEWED;
    }

    private void isPolicyActive() {

        if (status != PolicyStatus.ACTIVE) {

            throw new IllegalStateException("Operation allowed only for ACTIVE policies.");
        }
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

    public abstract String getPolicyDetails();
}