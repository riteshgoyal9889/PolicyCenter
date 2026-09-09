package com.wipfli.insurancemanagement.model;

import com.wipfli.insurancemanagement.exception.IllegalStatusChangeException;
import com.wipfli.insurancemanagement.exception.InvalidPolicyDataException;
import com.wipfli.insurancemanagement.exception.RenewalNotAllowedException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        this.expiryDate = startDate.plusYears((long) durationInYears);

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

        if (status != PolicyStatus.ACTIVE) {

            throw new IllegalStatusChangeException(
                    policyNumber,
                    "Policy "
                            + policyNumber + " is currently "
                            + status + " and cannot be expired."
            );
        }

        status = PolicyStatus.EXPIRED;
    }

    public void renewPolicy(LocalDate today) {
        if (status != PolicyStatus.ACTIVE) {
            throw new IllegalStatusChangeException(policyNumber,
                    "Policy " + policyNumber
                            + " is currently " + status
                            + " and cannot be renewed.");
        }
        long daysToExpiry = ChronoUnit.DAYS.between(today,expiryDate);

        if (daysToExpiry > 30) {
            throw new RenewalNotAllowedException(policyNumber, "Renewal requested " + (daysToExpiry - 30) + " days too early.");
        }
        if (claimCount >= 3) {
            throw new RenewalNotAllowedException(policyNumber, "Policy has 3 or more claims. Refer to underwriting.");
        }
        status = PolicyStatus.RENEWED;
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
    @Override

    public String toString() {
        return getPolicyDetails();
    }

    public abstract String getPolicyDetails();
}