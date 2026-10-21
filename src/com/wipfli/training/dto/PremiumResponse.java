package com.wipfli.training.dto;

public class PremiumResponse {
    private final String policyNumber;
    private final double standardPremium;
    private final double discountedPremium;

    public PremiumResponse(String policyNumber, double standardPremium, double discountedPremium) {
        this.policyNumber = policyNumber;
        this.standardPremium = standardPremium;
        this.discountedPremium = discountedPremium;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public double getStandardPremium() {
        return standardPremium;
    }

    public double getDiscountedPremium() {
        return discountedPremium;
    }
}


