package com.wipfli.insurancemanagement.model;
import com.wipfli.insurancemanagement.exception.InvalidPolicyDataException;
import java.time.LocalDate;


public class TruckPolicy extends Policy {

    private final double loadCapacityTons;

    public TruckPolicy(String policyNumber, PolicyOwner policyOwner, VehicleType vehicleType, int claimCount, LocalDate startDate, int durationInYears,  double loadCapacityTons) {

        super(policyNumber, policyOwner, vehicleType, claimCount, startDate, durationInYears);
        if (policyOwner.getAge() < 21) {
            throw new InvalidPolicyDataException(policyNumber,"Truck driver must be at least 21 years old.");
        }
        this.loadCapacityTons = loadCapacityTons;
    }

    @Override
    public String getPolicyDetails() {
        return "Policy Number: " + getPolicyNumber()
                + ", Owner: " + getPolicyOwner().getName()
                + ", Status: " + getStatus()
                + ", Load Capacity: " + loadCapacityTons + " Tons"
                + " claim count " + getClaimCount()
                + ", Expiry Date: " + getExpiryDate();

    }
}