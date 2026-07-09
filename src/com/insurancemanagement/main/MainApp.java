package com.insurancemanagement.main;

import com.insurancemanagement.model.PolicyOwner;
import com.insurancemanagement.model.Policy;
import com.insurancemanagement.model.VehicleType;
import com.insurancemanagement.service.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class MainApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        List<Policy> policies = new ArrayList<>();
        Set<String> customerNames = new TreeSet<>();

        System.out.println("===== Insurance Management System =====");

        int policyCount = readInt(scanner, "Enter number of policies: ");

        for (int i = 1; i <= policyCount; i++) {

            System.out.println();
            System.out.println("Enter details for policy " + i);

            try {

                String customerName = readString(scanner, "Enter customer name: ");
                int customerAge = readInt(scanner, "Enter customer age: ");

                PolicyOwner policyOwner = new PolicyOwner(customerName, customerAge);

                String policyNumber = readString(scanner, "Enter policy number: ");

                VehicleType vehicleType = readVehicleType(scanner);

                int claimCount = readInt(scanner, "Enter previous claims count: ");

                LocalDate startDate = readDate(scanner, "Enter policy start date yyyy-mm-dd: ");

                int duration = readInt(scanner, "Enter policy duration in years: ");

                Policy policy = new Policy(policyNumber, policyOwner, vehicleType, claimCount, startDate, duration);

                policies.add(policy);
                customerNames.add(policyOwner.getName());

                System.out.println("Policy added successfully.");

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
                System.out.println("Please re-enter this policy.");
                i--;
            }
        }

        System.out.println();
        System.out.println("===== Sorted Unique Customer Names =====");

        for (String name : customerNames) {
            System.out.println(name);
        }

        System.out.println();
        System.out.println("===== Policy Details =====");

        PolicyService policyService = new PolicyService();

        for (Policy policy : policies) {

            PremiumCalculable premiumCalculator = new VehiclePremiumCalculator();

            double premium = premiumCalculator.calculatePremium(policy);
            String status = policyService.checkPolicyStatus(policy);

            System.out.println("Policy Number: " + policy.getPolicyNumber());
            System.out.println("Customer Name: " + policy.getCustomer().getName());
            System.out.println("Customer Age: " + policy.getCustomer().getAge());
            System.out.println("Vehicle Type: " + policy.getVehicleType());
            System.out.println("Previous Claims: " + policy.getClaimCount());
            System.out.println("Start Date: " + policy.getStartDate());
            System.out.println("Duration: " + policy.getDurationInYears() + " years");
            System.out.println("Premium Amount: " + premium);
            System.out.println("Policy Status: " + status);
            System.out.println("--------------------------------");
        }

        System.out.println();
        System.out.println("===== Duplicate Policy Numbers =====");

        Set<Policy> policySet = new HashSet<>();
        boolean duplicateFound = false;

        for (Policy policy : policies) {
            if (!policySet.add(policy)) {
                System.out.println(policy.getPolicyNumber());
                duplicateFound = true;
            }
        }

        if (!duplicateFound) {
            System.out.println("No duplicate policies found.");
        }

        scanner.close();
    }

    private static String readString(Scanner scanner, String message) {

        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();

            if (!input.trim().isEmpty()) {
                return input;
            }

            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    private static int readInt(Scanner scanner, String message) {

        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static LocalDate readDate(Scanner scanner, String message) {

        while (true) {
            try {
                System.out.print(message);
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Please enter date in correct format: yyyy-mm-dd");
            }
        }
    }

    private static VehicleType readVehicleType(Scanner scanner) {

        while (true) {

            System.out.println("Select Vehicle Type");
            System.out.println("1. CAR");
            System.out.println("2. BIKE");
            System.out.println("3. TRUCK");

            System.out.print("Enter choice: ");

            try {

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {

                    case 1:
                        return VehicleType.CAR;

                    case 2:
                        return VehicleType.BIKE;

                    case 3:
                        return VehicleType.TRUCK;

                    default:
                        System.out.println("Please enter 1, 2 or 3.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number.");
            }
        }
    }
}