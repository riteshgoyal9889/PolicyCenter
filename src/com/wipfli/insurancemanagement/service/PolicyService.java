package com.wipfli.insurancemanagement.service;

import com.wipfli.insurancemanagement.model.Policy;
import com.wipfli.insurancemanagement.model.PolicyStatus;
import java.time.LocalDate;



public class PolicyService {

    public PolicyStatus checkPolicyStatus(Policy policy) {

        LocalDate startDate = policy.getStartDate();
        int duration = (int) policy.getDurationInYears();

        LocalDate expiryDate = startDate.plusYears(duration);

        LocalDate today = LocalDate.now();

        if (today.isAfter(expiryDate)) {
            return PolicyStatus.EXPIRED;
        }

        LocalDate renewalDate = expiryDate.minusDays(30);
        if (today.isAfter(renewalDate)) {
            return PolicyStatus.RENEWED;
        }
        return PolicyStatus.ACTIVE;
    }
}