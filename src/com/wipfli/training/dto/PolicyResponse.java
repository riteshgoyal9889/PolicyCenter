package com.wipfli.training.dto;

import com.wipfli.training.model.Policy;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

public class PolicyResponse {

    private String policyNumber;
    private String vehicleType;
    private String status;
    private String customerName;
    private int customerAge;
    private LocalDate expiryDate;
    private int previousClaims;
    private String details;
    private APIStatus apiStatus;

    public PolicyResponse() {
    }

    public static PolicyResponse from(Policy policy) {
        return from(policy, HttpStatus.OK, "Policy retrieved successfully");
    }

    public static PolicyResponse from(Policy policy, HttpStatus httpStatus, String message) {
        PolicyResponse response = new PolicyResponse();
        response.policyNumber = policy.getPolicyNumber();
        response.vehicleType = policy.getVehicleType().name();
        response.status = policy.getStatus().name();
        response.customerName = policy.getPolicyOwner().getName();
        response.customerAge = policy.getPolicyOwner().getAge();
        response.expiryDate = policy.getExpiryDate();
        response.previousClaims = policy.getClaimCount();
        response.details = policy.getPolicyDetails();
        response.apiStatus = APIStatus.of(httpStatus, message);
        return response;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerAge() {
        return customerAge;
    }

    public void setCustomerAge(int customerAge) {
        this.customerAge = customerAge;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getPreviousClaims() {
        return previousClaims;
    }

    public void setPreviousClaims(int previousClaims) {
        this.previousClaims = previousClaims;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public APIStatus getApiStatus() {
        return apiStatus;
    }

    public void setApiStatus(APIStatus apiStatus) {
        this.apiStatus = apiStatus;
    }
}
