package com.wipfli.training.repository;

import com.wipfli.training.model.Policy;

import java.util.List;
import java.util.Optional;

public interface PolicyRepository {

    void save(Policy policy);

    Optional<Policy> findByNumber(String policyNumber);

    boolean existsByNumber(String policyNumber);

    List<Policy> findAll();
}



