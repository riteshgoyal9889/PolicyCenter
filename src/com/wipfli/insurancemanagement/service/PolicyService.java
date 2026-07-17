package com.wipfli.insurancemanagement.service;

import com.wipfli.insurancemanagement.model.Policy;

import java.time.LocalDate;



public class PolicyService {

    public String checkPolicyStatus(Policy policy) {

        LocalDate startDate = policy.getStartDate();
        int duration = (int) policy.getDurationInYears();

        LocalDate expiryDate = startDate.plusYears(duration);

        LocalDate today = LocalDate.now();

        if (today.isAfter(expiryDate)) {
            return "EXPIRED";
        }

        LocalDate renewalDate = expiryDate.minusDays(30);
        if (today.isAfter(renewalDate)) {
            return "DUE FOR RENEWAL";
        }
        return "ACTIVE";
    }
}