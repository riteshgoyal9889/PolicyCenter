package com.wipfli.insurancemanagement.service;

import com.wipfli.insurancemanagement.exception.DuplicatePolicyNumberException;
import com.wipfli.insurancemanagement.exception.PolicyNotFoundException;
import com.wipfli.insurancemanagement.model.Policy;
import com.wipfli.insurancemanagement.model.VehicleType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.EnumMap;
import java.util.TreeMap;

public class PolicyRegister {

    private final Map<String, Policy> policiesByNumber = new HashMap<>();
    private final Map<String, List<Policy>> policiesByCustomer = new HashMap<>();
    private final Map<VehicleType, List<Policy>> policiesByVehicleType =new EnumMap<>(VehicleType.class);
    private final TreeMap<LocalDate, List<Policy>> policiesByExpiryDate = new TreeMap<>();

    public void add(Policy policy) {

        if (policiesByNumber.containsKey(policy.getPolicyNumber())) {
            throw new DuplicatePolicyNumberException(policy.getPolicyNumber(), "Policy number already exists.");
        }
        policiesByNumber.put(policy.getPolicyNumber(), policy);

        String customerName = policy.getPolicyOwner().getName();
        if (!policiesByCustomer.containsKey(customerName)) {
            policiesByCustomer.put(customerName, new ArrayList<>());
        }
        policiesByCustomer.get(customerName).add(policy);

        VehicleType vehicleType = policy.getVehicleType();
        if (!policiesByVehicleType.containsKey(vehicleType)) {
            policiesByVehicleType.put(vehicleType, new ArrayList<>());
        }
        policiesByVehicleType.get(vehicleType).add(policy);

        LocalDate expiryDate = policy.getExpiryDate();
        if (!policiesByExpiryDate.containsKey(expiryDate)) {
            policiesByExpiryDate.put(expiryDate, new ArrayList<>());
        }
        policiesByExpiryDate.get(expiryDate).add(policy);
    }

    public Policy findByNumber(String policyNumber) throws PolicyNotFoundException {
        Policy policy = policiesByNumber.get(policyNumber);

        if (policy == null) {
            throw new PolicyNotFoundException("Policy "+ policyNumber+ " not found.");
        }
        return policy;
    }
    public List<Policy> findByCustomer(String customerName) {

        List<Policy> policies = policiesByCustomer.get(customerName);

        if (policies == null) {
            return new ArrayList<>();
        }
        return policies;
    }

    public List<Policy> findByVehicleType(VehicleType vehicleType) {

        List<Policy> policies = policiesByVehicleType.get(vehicleType);
        if (policies == null) {
            return new ArrayList<>();
        }
        return policies;
    }

    public List<Policy> findExpiringWithin(int days, LocalDate today) {

        List<Policy> result = new ArrayList<>();
        LocalDate limitDate = today.plusDays(days);
        for (List<Policy> policies : policiesByExpiryDate.subMap(today, true, limitDate, true).values()) {
            result.addAll(policies);
        }
        return result;
    }

    public Map<VehicleType, Double> totalPremiumByVehicleType() {

        Map<VehicleType, Double> summary = new HashMap<>();
        PremiumCalculable calculator = new StandardPremiumCalculator();

        for (VehicleType vehicleType : policiesByVehicleType.keySet()) {
            double totalPremium = 0.0;
            List<Policy> policies = policiesByVehicleType.get(vehicleType);

            for (Policy policy : policies) {
                totalPremium += calculator.calculatePremium(policy);
            }
            summary.put(vehicleType, totalPremium);
        }
        return summary;
    }
}