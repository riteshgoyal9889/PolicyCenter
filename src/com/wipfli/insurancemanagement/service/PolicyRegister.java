package com.wipfli.insurancemanagement.service;

import com.wipfli.insurancemanagement.exception.DuplicatePolicyNumberException;
import com.wipfli.insurancemanagement.exception.PolicyNotFoundException;
import com.wipfli.insurancemanagement.model.Policy;

import java.util.HashMap;
import java.util.Map;

public class PolicyRegister {

    private final Map<String, Policy> policies = new HashMap<>();

    public void add(Policy policy) {
        if (policies.containsKey(policy.getPolicyNumber())) {

            throw new DuplicatePolicyNumberException(policy.getPolicyNumber(), "Policy number already exists.");
        }

        policies.put(policy.getPolicyNumber(), policy);
    }

    public Policy findByNumber(String policyNumber)
            throws PolicyNotFoundException {
        Policy policy = policies.get(policyNumber);

        if (policy == null) {
            throw new PolicyNotFoundException("Policy " + policyNumber+ " not found.");
        }
        return policy;
    }
}