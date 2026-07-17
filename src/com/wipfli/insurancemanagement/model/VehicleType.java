package com.wipfli.insurancemanagement.model;

public enum VehicleType {

    BIKE(300),
    CAR(500),
    TRUCK(800);


    private final double basePremium;

    VehicleType(double basePremium) {
        this.basePremium = basePremium;
    }

    public double getBasePremium() {
        return basePremium;
    }
}
