package com.insurancemanagement.service;

import com.insurancemanagement.model.Policy;

public interface PremiumCalculable {

    double calculatePremium(Policy policy);
}
