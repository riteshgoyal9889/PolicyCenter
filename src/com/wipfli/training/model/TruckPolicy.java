package com.wipfli.training.model;

import com.wipfli.training.exception.InvalidPolicyDataException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("TRUCK")
public class TruckPolicy extends Policy {

    @Column(name = "load_capacity_tons", nullable = true)
    private double loadCapacityTons;

    protected TruckPolicy() {
        super();
    }

    public TruckPolicy(String policyNumber, PolicyHolder policyHolder, VehicleType vehicleType,
                      int claimCount, LocalDate startDate, int durationInYears, double loadCapacityTons) {
        super(policyNumber, policyHolder, vehicleType, claimCount, startDate, durationInYears);

        if (policyHolder.getAge() < 21) {
            throw new InvalidPolicyDataException(policyNumber, "Truck driver must be at least 21 years old.");
        }
        this.loadCapacityTons = loadCapacityTons;
    }

    public double getLoadCapacityTons() {
        return loadCapacityTons;
    }

    @Override
    public String getPolicyDetails() {
        return "Policy Number: " + getPolicyNumber()
                + ", Owner: " + getPolicyHolder().getName()
                + ", Status: " + getStatus()
                + ", Vehicle Type: " + getVehicleType()
                + ", Load Capacity: " + loadCapacityTons + " Tons"
                + ", claim count " + getClaimCount()
                + ", Expiry Date: " + getExpiryDate();
    }
}