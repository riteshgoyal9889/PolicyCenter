package com.wipfli.insurancemanagement.model;

import com.wipfli.insurancemanagement.exception.InvalidPolicyDataException;

public class PolicyOwner {

    private final String name;
    private final int age;

    public PolicyOwner(String name, int age) {

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
        this.name = name;
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
}