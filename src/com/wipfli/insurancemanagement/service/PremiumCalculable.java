package com.wipfli.insurancemanagement.service;

import com.wipfli.insurancemanagement.model.Policy;

public interface PremiumCalculable {

    double calculatePremium(Policy policy);
}
