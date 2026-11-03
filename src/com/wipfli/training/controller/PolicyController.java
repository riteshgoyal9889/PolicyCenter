package com.wipfli.training.controller;

import com.wipfli.training.dto.CreateBikePolicyRequest;
import com.wipfli.training.dto.CreateCarPolicyRequest;
import com.wipfli.training.dto.CreateTruckPolicyRequest;
import com.wipfli.training.dto.PolicyResponse;
import com.wipfli.training.dto.PremiumResponse;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.Policy;
import com.wipfli.training.model.PolicyHolder;
import com.wipfli.training.model.VehicleType;
import com.wipfli.training.service.PolicyFactory;
import com.wipfli.training.service.PolicyRegister;
import com.wipfli.training.service.PremiumCalculable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private final PolicyRegister policyRegister;
    private final PremiumCalculable standardCalculator;
    private final PremiumCalculable noClaimCalculator;

    public PolicyController(PolicyRegister policyRegister,
                            @Qualifier("standardPremiumCalculator") PremiumCalculable standardCalculator,
                            @Qualifier("noClaimBonusCalculator") PremiumCalculable noClaimCalculator) {
        this.policyRegister = policyRegister;
        this.standardCalculator = standardCalculator;
        this.noClaimCalculator = noClaimCalculator;
    }

    @PostMapping("/car")
    public ResponseEntity<PolicyResponse> createCarPolicy(@RequestBody CreateCarPolicyRequest request) {
        PolicyHolder customer = new PolicyHolder(request.getOwnerName(), request.getOwnerAge());
        Policy policy = PolicyFactory.createCarPolicy(
                request.getPolicyNumber(), customer, VehicleType.CAR,
                request.getClaimCount(), request.getStartDate(), request.getDurationInYears(),
                request.getRegistrationNumber());

        policyRegister.add(policy);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PolicyResponse.from(policy, HttpStatus.CREATED, "Policy created successfully"));
    }

    @PostMapping("/truck")
    public ResponseEntity<PolicyResponse> createTruckPolicy(@RequestBody CreateTruckPolicyRequest request) {
        PolicyHolder customer = new PolicyHolder(request.getOwnerName(), request.getOwnerAge());
        Policy policy = PolicyFactory.createTruckPolicy(
                request.getPolicyNumber(), customer, VehicleType.TRUCK,
                request.getClaimCount(), request.getStartDate(), request.getDurationInYears(),
                request.getLoadCapacityTons());

        policyRegister.add(policy);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PolicyResponse.from(policy, HttpStatus.CREATED, "Policy created successfully"));
    }

    @PostMapping("/bike")
    public ResponseEntity<PolicyResponse> createBikePolicy(@RequestBody CreateBikePolicyRequest request) {
        PolicyHolder customer = new PolicyHolder(request.getOwnerName(), request.getOwnerAge());
        Policy policy = PolicyFactory.createBikePolicy(
                request.getPolicyNumber(), customer, VehicleType.BIKE,
                request.getClaimCount(), request.getStartDate(), request.getDurationInYears(),
                request.getEngineCC());

        policyRegister.add(policy);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(PolicyResponse.from(policy, HttpStatus.CREATED, "Policy created successfully"));
    }

    @GetMapping("/{policyNumber}")
    public ResponseEntity<PolicyResponse> getPolicy(@PathVariable String policyNumber) throws PolicyNotFoundException {
        Policy policy = policyRegister.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + policyNumber));
        return ResponseEntity.ok(PolicyResponse.from(policy, HttpStatus.OK, "Policy retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<List<PolicyResponse>> getAllPolicies() {
        List<PolicyResponse> responses = policyRegister.findAllPolicies().stream()
                .map(policy -> PolicyResponse.from(policy, HttpStatus.OK, "Policy retrieved successfully"))
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{policyNumber}/claims")
    public ResponseEntity<PolicyResponse> recordClaim(@PathVariable String policyNumber) throws PolicyNotFoundException {
        policyRegister.recordClaim(policyNumber);
        Policy policy = policyRegister.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + policyNumber));
        return ResponseEntity.ok(PolicyResponse.from(policy, HttpStatus.OK, "Claim recorded successfully"));
    }

    @PostMapping("/{policyNumber}/expire")
    public ResponseEntity<PolicyResponse> expirePolicy(@PathVariable String policyNumber) throws PolicyNotFoundException {
        policyRegister.expirePolicy(policyNumber);
        Policy policy = policyRegister.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + policyNumber));
        return ResponseEntity.ok(PolicyResponse.from(policy, HttpStatus.OK, "Policy expired successfully"));
    }

    @PostMapping("/{policyNumber}/renew")
    public ResponseEntity<PolicyResponse> renewPolicy(
            @PathVariable String policyNumber,
            @RequestParam(required = false) String on)
            throws PolicyNotFoundException {
        LocalDate renewalDate = (on != null && !on.isBlank())
                ? LocalDate.parse(on, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : LocalDate.now();

        policyRegister.renewPolicy(policyNumber, renewalDate);
        Policy policy = policyRegister.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + policyNumber));
        return ResponseEntity.ok(PolicyResponse.from(policy, HttpStatus.OK, "Policy renewed successfully"));
    }

    @GetMapping("/{policyNumber}/premium")
    public ResponseEntity<PremiumResponse> getPremium(@PathVariable String policyNumber) throws PolicyNotFoundException {
        Policy policy = policyRegister.findByPolicyNumber(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + policyNumber));

        double standard = standardCalculator.calculatePremium(policy);
        double discounted = noClaimCalculator.calculatePremium(policy);
        return ResponseEntity.ok(new PremiumResponse(policyNumber, standard, discounted));
    }
}
