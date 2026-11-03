package com.wipfli.training.service;

import com.wipfli.training.model.*;

import java.time.LocalDate;

public final class PolicyFactory {

    public static Policy createCarPolicy(
            String policyNumber, PolicyHolder policyHolder, VehicleType vehicleType, int claimCount, LocalDate startDate, int durationInYears, String registrationNumber) {

        return new CarPolicy(policyNumber, policyHolder, vehicleType, claimCount, startDate, durationInYears, registrationNumber);
    }

    public static Policy createBikePolicy(
            String policyNumber, PolicyHolder policyHolder, VehicleType vehicleType, int claimCount, LocalDate startDate, int durationInYears, int engineCC) {

        return new BikePolicy(policyNumber, policyHolder, vehicleType, claimCount, startDate, durationInYears, engineCC);
    }

    public static Policy createTruckPolicy(
            String policyNumber, PolicyHolder policyHolder, VehicleType vehicleType, int claimCount, LocalDate startDate, int durationInYears, double loadCapacityTons) {

        return new TruckPolicy(policyNumber, policyHolder, vehicleType, claimCount, startDate, durationInYears, loadCapacityTons);
    }
}
