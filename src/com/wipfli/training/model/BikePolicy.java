package com.wipfli.training.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("BIKE")
public class BikePolicy extends Policy {

    @Column(name = "engine_cc", nullable = true)
    private int engineCC;

    protected BikePolicy() {
        super();
    }

    public BikePolicy(String policyNumber, PolicyHolder policyHolder, VehicleType vehicleType,
                     int claimCount, LocalDate startDate, int durationInYears, int engineCC) {
        super(policyNumber, policyHolder, vehicleType, claimCount, startDate, durationInYears);
        this.engineCC = engineCC;
    }

    public int getEngineCC() {
        return engineCC;
    }

    @Override
    public String getPolicyDetails() {
        return "Policy Number: " + getPolicyNumber()
                + ", Owner: " + getPolicyHolder().getName()
                + ", Status: " + getStatus()
                + ", Vehicle Type: " + getVehicleType()
                + ", Engine CC: " + engineCC
                + ", claim count " + getClaimCount()
                + ", Expiry Date: " + getExpiryDate();
    }
}