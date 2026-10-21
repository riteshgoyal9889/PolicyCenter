package com.wipfli.training.dto;

import java.time.LocalDate;

public class CreateTruckPolicyRequest {

    private String policyNumber;
    private String ownerName;
    private int ownerAge;
    private int claimCount;
    private LocalDate startDate;
    private int durationInYears;
    private double loadCapacityTons;

    public CreateTruckPolicyRequest() {
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }


    public int getOwnerAge() {
        return ownerAge;
    }


    public int getClaimCount() {
        return claimCount;
    }

    public void setClaimCount(int claimCount) {
        this.claimCount = claimCount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getDurationInYears() {
        return durationInYears;
    }


    public double getLoadCapacityTons() {
        return loadCapacityTons;
    }

    public void setLoadCapacityTons(double loadCapacityTons) {
        this.loadCapacityTons = loadCapacityTons;
    }
}
