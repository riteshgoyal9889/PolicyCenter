package com.wipfli.training.service;

import com.wipfli.training.exception.DuplicatePolicyNumberException;
import com.wipfli.training.exception.PolicyNotFoundException;
import com.wipfli.training.model.Policy;
import com.wipfli.training.model.VehicleType;
import com.wipfli.training.repository.PolicyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PolicyRegister {

    private final PolicyRepository repository;
    private final PremiumCalculatorResolver premiumCalculatorResolver;

    public PolicyRegister(PremiumCalculatorResolver premiumCalculatorResolver, PolicyRepository repository) {
        this.premiumCalculatorResolver = premiumCalculatorResolver;
        this.repository = repository;
    }

    public void add(Policy policy) {
        if (repository.existsById(policy.getPolicyNumber())) {
            throw new DuplicatePolicyNumberException(policy.getPolicyNumber(), "Policy number already exists");
        }
        repository.save(policy);
    }

    public Optional<Policy> findByPolicyNumber(String policyNumber) {
        return repository.findById(policyNumber);
    }

    public Policy findByNumber(String policyNumber) throws PolicyNotFoundException {
        return repository.findById(policyNumber)
                .orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + policyNumber));
    }

    public List<Policy> findPoliciesByCustomerName(String customerName) {
        return repository.findByPolicyHolderNameIgnoreCase(customerName);
    }

    public double calculateTotalPremiumByVehicleType(VehicleType vehicleType) {
        return repository.findByVehicleType(vehicleType)
                .stream()
                .mapToDouble(policy -> premiumCalculatorResolver.resolve(policy).calculatePremium(policy))
                .sum();
    }

    public Map<VehicleType, Long> countPoliciesByVehicleType() {
        return repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Policy::getVehicleType, Collectors.counting()));
    }

    public List<Policy> findPoliciesExpiringWithin(int days, LocalDate referenceDate) {
        LocalDate end = referenceDate.plusDays(days);
        return repository.findByExpiryDateBetween(referenceDate, end)
                .stream()
                .sorted(Comparator.comparing(Policy::getExpiryDate))
                .toList();
    }

    public Optional<String> findCustomerWithMostPoliciesUsingStream() {
        return repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        policy -> policy.getPolicyHolder().getName(),
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    public List<Policy> findTopFiveHighestPremiumPoliciesUsingStream() {
        return repository.findAll()
                .stream()
                .sorted(Comparator.comparingDouble((Policy policy) ->
                        premiumCalculatorResolver.resolve(policy).calculatePremium(policy)).reversed())
                .limit(5)
                .toList();
    }

    public List<Policy> findAllPolicies() {
        return repository.findAll();
    }

    public List<Policy> getAll() {
        return repository.findAll();
    }

    public List<Policy> findByVehicleType(VehicleType vehicleType) {
        return repository.findByVehicleType(vehicleType);
    }

    public void recordClaim(String policyNumber) throws PolicyNotFoundException {
        Policy policy = findByNumber(policyNumber);
        policy.recordClaim();
        repository.save(policy);
    }

    public void expirePolicy(String policyNumber) throws PolicyNotFoundException {
        Policy policy = findByNumber(policyNumber);
        policy.expire();
        repository.save(policy);
    }

    public void renewPolicy(String policyNumber) throws PolicyNotFoundException {
        renewPolicy(policyNumber, LocalDate.now());
    }

    public void renewPolicy(String policyNumber, LocalDate today) throws PolicyNotFoundException {
        Policy policy = findByNumber(policyNumber);
        policy.renew(today);
        repository.save(policy);
    }

    public double calculatePremium(String policyNumber) throws PolicyNotFoundException {
        Policy policy = findByNumber(policyNumber);
        return premiumCalculatorResolver.resolve(policy).calculatePremium(policy);
    }
}
