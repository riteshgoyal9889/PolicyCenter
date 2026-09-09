package com.wipfli.insurancemanagement.service;

import com.wipfli.insurancemanagement.model.*;

import java.time.LocalDate;

public final class PolicyFactory {

    public static Policy createCarPolicy(
            String policyNumber, PolicyOwner policyOwner, VehicleType vehicleType, int claimCount, LocalDate startDate, int durationInYears, String registrationNumber) {

        return new CarPolicy(policyNumber, policyOwner, vehicleType, claimCount, startDate, durationInYears, registrationNumber);
    }

    public static Policy createBikePolicy(
            String policyNumber, PolicyOwner policyOwner, VehicleType vehicleType, int claimCount, LocalDate startDate, int durationInYears, int engineCC) {

        return new BikePolicy(policyNumber, policyOwner, vehicleType, claimCount, startDate, durationInYears, engineCC);
    }

    public static Policy createTruckPolicy(
            String policyNumber, PolicyOwner policyOwner, VehicleType vehicleType, int claimCount, LocalDate startDate, int durationInYears, double loadCapacityTons) {

        return new TruckPolicy(policyNumber, policyOwner, vehicleType, claimCount, startDate, durationInYears, loadCapacityTons);
    }
}