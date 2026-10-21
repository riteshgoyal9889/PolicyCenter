package com.wipfli.training.repository;

import com.wipfli.training.model.Policy;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryPolicyRepository implements PolicyRepository {

    private final Map<String, Policy> policies = new HashMap<>();

    @Override
    public void save(Policy policy) {
        policies.put(policy.getPolicyNumber(), policy);
    }

    @Override
    public Optional<Policy> findByNumber(String policyNumber) {
        return Optional.ofNullable(
                policies.get(policyNumber)
        );
    }

    @Override
    public boolean existsByNumber(String policyNumber) {
        return policies.containsKey(policyNumber);
    }

    @Override
    public List<Policy> findAll() {
        return new ArrayList<>(policies.values());
    }
}
