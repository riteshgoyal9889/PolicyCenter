package com.wipfli.training.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("CAR")
public class CarPolicy extends Policy {

    @Column(name = "registration_number", nullable = true)
    private String registrationNumber;

    protected CarPolicy() {
        super();
    }

    public CarPolicy(String policyNumber, PolicyHolder policyHolder, VehicleType vehicleType,
                    int claimCount, LocalDate startDate, int durationInYears, String registrationNumber) {
        super(policyNumber, policyHolder, vehicleType, claimCount, startDate, durationInYears);
        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @Override
    public String getPolicyDetails() {
        return "Policy Number: " + getPolicyNumber()
                + ", Owner: " + getPolicyHolder().getName() + ","
                + " Status: " + getStatus() + ","
                + ", Vehicle Type: " + getVehicleType()
                + " Registration Number: " + registrationNumber
                + " claim count " + getClaimCount()
                + ", Expiry Date: " + getExpiryDate();
    }
}