package com.wipfli.training.service;

import com.wipfli.training.model.Policy;

public interface PremiumCalculable {

    double calculatePremium(Policy policy);
}

