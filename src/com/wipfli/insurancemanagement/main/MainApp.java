package com.wipfli.insurancemanagement.main;

import com.wipfli.insurancemanagement.model.*;
import com.wipfli.insurancemanagement.service.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MainApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Policy> policies = new ArrayList<>();


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
                PolicyService policyService = new PolicyService();

                Policy policy = new Policy(policyNumber, policyOwner, vehicleType, claimCount, startDate, duration);

                PolicyValidator validator = new PolicyValidator();
                validator.validate(policy);
                policies.add(policy);

                showPolicyMenu(scanner, policy);

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());
                System.out.println("Please re-enter this policy.");
                i--;
            }

        }
        System.out.println();
        System.out.println("===== Policy Details =====");

        PolicyService policyService = new PolicyService();


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

    private static void showPolicyMenu(Scanner scanner, Policy policy) {

        PremiumCalculable standardCalculator = new StandardPremiumCalculator();

        PremiumCalculable noClaimCalculator = new NoClaimBonusCalculator();

        PolicyService policyService = new PolicyService();

        while (true) {

            System.out.println("\n===== POLICY MENU =====");

            System.out.println("1. Calculate Premium");
            System.out.println("2. Expire Policy");
            System.out.println("3. Renew Policy");
            System.out.println("4. Record Claim");
            System.out.println("5. Exit");

            int choice = readInt(scanner, "Enter choice: ");

            try {

                switch (choice) {

                    case 1:
                        double standardPremium = standardCalculator.calculatePremium(policy);
                        double bonusPremium = noClaimCalculator.calculatePremium(policy);
                        System.out.println("\nStandard Premium : " + standardPremium);
                        System.out.println("No Claim Bonus Premium : " + bonusPremium);
                        break;

                    case 2:
                        policy.expirePolicy();
                        System.out.println("Policy expired successfully.");
                        break;

                    case 3:
                        policy.renewPolicy();
                        System.out.println("Policy renewed successfully.");
                        break;

                    case 4:
                        policy.recordClaim();
                        System.out.println("Claim recorded successfully.");
                        System.out.println("Total Claims : " + policy.getClaimCount());
                        break;

                    case 5:
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }

            } catch (Exception e) {

                System.out.println(e.getMessage());
            }
        }
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
                int value = Integer.parseInt(scanner.nextLine());
                if (value >=0) {
                    return value;
                }
                System.out.println("Value must be greater than 0.");
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