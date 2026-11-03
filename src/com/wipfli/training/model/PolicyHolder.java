package com.wipfli.training.model;

import com.wipfli.training.exception.InvalidPolicyDataException;
import jakarta.persistence.*;

@Entity
@Table(name = "policy_holder")
public class PolicyHolder {

    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age", nullable = false)
    private int age;

    protected PolicyHolder() {
        // JPA requires no-arg constructor
    }

    public PolicyHolder(String name, int age) {
        this(generateUserId(name, age), name, age);
    }

    public PolicyHolder(String userId, String name, int age) {
        if (userId == null || userId.isBlank()) {
            throw new InvalidPolicyDataException("UNKNOWN", "User ID cannot be empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidPolicyDataException("UNKNOWN", "Customer name cannot be empty.");
        }
        for (char ch : name.toCharArray()) {
            if (!Character.isLetter(ch) && ch != ' ') {
                throw new InvalidPolicyDataException("UNKNOWN", "Name must contain only letters and spaces.");
            }
        }
        if (age < 18 || age > 100) {
            throw new InvalidPolicyDataException("UNKNOWN", "Age must be between 18 and 100.");
        }
        this.userId = userId.trim();
        this.name = name.trim();
        this.age = age;
    }

    private static String generateUserId(String name, int age) {
        String clean = (name == null ? "UNKNOWN" : name.trim().replaceAll("\\s+", "")).toUpperCase();
        return clean.isEmpty() ? "USER-" + age : clean + "-" + age;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
