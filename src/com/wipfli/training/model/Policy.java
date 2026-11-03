package com.wipfli.training.model;

import com.wipfli.training.exception.IllegalStatusChangeException;
import com.wipfli.training.exception.InvalidPolicyDataException;
import com.wipfli.training.exception.RenewalNotAllowedException;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "policy_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "policy")
public abstract class Policy {

    @Id
    @Column(name = "policy_number", nullable = false, unique = true)
    private String policyNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id", nullable = false)
    private PolicyHolder policyHolder;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;

    @Column(name = "claim_count", nullable = false)
    private int claimCount;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "duration_in_years", nullable = false)
    private double durationInYears;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PolicyStatus status;

    protected Policy() {
        // JPA requires this
    }

    public Policy(String policyNumber, PolicyHolder policyHolder, VehicleType vehicleType,
                  int claimCount, LocalDate startDate, double durationInYears) {
        if (policyNumber == null || policyNumber.trim().isEmpty()) {
            throw new InvalidPolicyDataException("UNKNOWN", "Policy number cannot be empty.");
        }
        if (policyHolder == null) {
            throw new InvalidPolicyDataException(policyNumber, "Policy holder cannot be null.");
        }
        if (startDate == null) {
            throw new InvalidPolicyDataException(policyNumber, "Start date cannot be null.");
        }
        if (durationInYears <= 0) {
            throw new InvalidPolicyDataException(policyNumber, "Duration in years must be positive.");
        }

        this.policyNumber = policyNumber.trim();
        this.policyHolder = policyHolder;
        this.vehicleType = vehicleType;
        this.claimCount = claimCount;
        this.startDate = startDate;
        this.durationInYears = durationInYears;
        this.expiryDate = startDate.plusYears((long) durationInYears);

        if (expiryDate.isBefore(LocalDate.now())) {
            throw new InvalidPolicyDataException(policyNumber, "Policy expiry date cannot be in the past.");
        }

        this.status = PolicyStatus.ACTIVE;
    }

    public Policy(String policyNumber, PolicyHolder policyHolder, VehicleType vehicleType,
                  LocalDate startDate, int durationInYears) {
        this(policyNumber, policyHolder, vehicleType, 0, startDate, durationInYears);
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public double getDurationInYears() {
        return durationInYears;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void recordClaim() {
        claimCount++;
    }

    public void expire() {
        if (status != PolicyStatus.ACTIVE) {
            throw new IllegalStatusChangeException(
                    policyNumber,
                    "Policy " + policyNumber + " is currently " + status + " and cannot be expired."
            );
        }
        status = PolicyStatus.EXPIRED;
    }

    public void renew(LocalDate today) {
        if (status != PolicyStatus.ACTIVE) {
            throw new IllegalStatusChangeException(
                    policyNumber,
                    "Policy " + policyNumber + " is currently " + status + " and cannot be renewed."
            );
        }

        long daysToExpiry = ChronoUnit.DAYS.between(today, expiryDate);

        if (daysToExpiry > 30) {
            throw new RenewalNotAllowedException(
                    policyNumber,
                    "Renewal requested " + (daysToExpiry - 30) + " days too early."
            );
        }

        if (claimCount >= 3) {
            throw new RenewalNotAllowedException(
                    policyNumber,
                    "Policy has 3 or more claims. Refer to underwriting."
            );
        }

        status = PolicyStatus.RENEWED;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Policy)) return false;
        Policy other = (Policy) obj;
        return Objects.equals(policyNumber, other.policyNumber);
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