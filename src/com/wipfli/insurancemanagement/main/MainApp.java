package com.wipfli.insurancemanagement.main;

import com.wipfli.insurancemanagement.model.*;
import com.wipfli.insurancemanagement.service.*;
import com.wipfli.insurancemanagement.exception.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MainApp {

    private final Scanner scanner = new Scanner(System.in);
    private final PolicyRegister register = new PolicyRegister();
    private final PolicyValidator validator = new PolicyValidator();
    private Policy selectedPolicy;

    public static void main(String[] args) {

        new MainApp().run();
    }

    public void run() {

        seedData();

        while (true) {
            printMenu();
            int choice = readInt(scanner, "Enter choice: ");
            switch (choice) {
                case 1:
                    handleAddPolicy();
                    break;
                case 2:
                    handleSelectPolicy();
                    break;

                case 3:
                    handleCalculatePremium();
                    break;

                case 4:
                    handleExpirePolicy();
                    break;

                case 5:
                    handleRenewPolicy();
                    break;

                case 6:
                    handleRecordClaim();
                    break;

                case 7:
                    handleShowPolicyDetails();
                    break;

                case 8:
                    handleViewByCustomer();
                    break;

                case 9:
                    handleViewByVehicleType();
                    break;

                case 10:
                    handleViewExpiringSoon();
                    break;

                case 11:
                    handleViewSummary();
                    break;

                case 12:
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    private void seedData() {

        PolicyOwner ritesh = new PolicyOwner("Ritesh", 25);
        PolicyOwner amit = new PolicyOwner("Amit", 30);
        PolicyOwner rahul = new PolicyOwner("Rahul", 35);
        PolicyOwner sneha = new PolicyOwner("Sneha", 28);

        register.add(new CarPolicy("POL-101",ritesh, VehicleType.CAR, 0,  LocalDate.now(), 1, "MH12AB1234"));
        register.add(new BikePolicy("POL-102", ritesh,  VehicleType.BIKE, 1, LocalDate.now(), 2, 200));

        register.add(new TruckPolicy("POL-103", amit, VehicleType.TRUCK,0, LocalDate.now(), 1, 12));
        register.add(new CarPolicy("POL-104", rahul, VehicleType.CAR, 2, LocalDate.now(), 3, "MH14XY9999"));
        register.add(new BikePolicy("POL-105", sneha, VehicleType.BIKE, 0, LocalDate.now(), 1, 150));
        register.add(new TruckPolicy("POL-106", sneha, VehicleType.TRUCK, 1, LocalDate.now(), 2, 15));
        System.out.println("6 sample policies loaded successfully.");
    }
    private void printMenu() {
        System.out.println("\n===== POLICY MANAGEMENT SYSTEM =====");

        System.out.println("1. Add Policy");
        System.out.println("2. Select Policy");
        System.out.println("3. Calculate Premium");
        System.out.println("4. Expire Policy");
        System.out.println("5. Renew Policy");
        System.out.println("6. Record Claim");
        System.out.println("7. Show Policy Details");
        System.out.println("8. View Policies By Customer");
        System.out.println("9. View Policies By Vehicle Type");
        System.out.println("10. View Policies Expiring Soon");
        System.out.println("11. View Premium Summary");
        System.out.println("12. Exit");
    }
    private void handleAddPolicy() {
        try {
            String customerName = readCustomerName(scanner,validator);
            int customerAge = readCustomerAge(scanner, validator);

            PolicyOwner policyOwner = new PolicyOwner(customerName, customerAge);

            String policyNumber = readString(scanner, "Enter policy number: ");
            validator.validatePolicyNumber(policyNumber);
            VehicleType vehicleType = readVehicleType(scanner);

            int claimCount = readClaimCount(scanner, validator,policyNumber);
            LocalDate startDate = readDate(scanner, "Enter policy start date yyyy-mm-dd: ");
            int duration = readInt(scanner, "Enter policy duration in years: ");
            validator.validateExpiryDate(policyNumber,startDate,duration);

            Policy policy;
            switch(vehicleType) {

                case CAR:
                    String registrationNumber = readString( scanner,"Enter Registration Number: ");
                    System.out.println("-----Policy for CAR-----");
                    policy = new CarPolicy(policyNumber,policyOwner,vehicleType,claimCount,startDate,duration, registrationNumber);
                    break;

                case BIKE:
                    int engineCC = readInt(scanner, "Enter Engine CC: ");
                    System.out.println("-----Policy for BIKE-----");
                    policy = new BikePolicy(policyNumber, policyOwner, vehicleType, claimCount, startDate, duration, engineCC);
                    break;

                case TRUCK:
                    double loadCapacity = readDouble(scanner, "Enter Load Capacity: ");
                    System.out.println("-----Policy for TRUCK-----");
                    policy = new TruckPolicy(policyNumber, policyOwner, vehicleType, claimCount, startDate, duration, loadCapacity);
                    break;

                default:
                    throw new IllegalArgumentException("Invalid vehicle type.");
            }
            register.add(policy);

            System.out.println("Policy added successfully.");
            System.out.println();

        } catch (PolicyBusinessException e) {
            System.out.println(e.getClass().getSimpleName() + " : " + e.getMessage());
            System.out.println("Please re-enter this policy.");
        }
    }
    private void handleSelectPolicy() {

        try {
            String policyNumber = readString(scanner, "Enter Policy Number: ");
            selectedPolicy = register.findByNumber(policyNumber);
            System.out.println("Policy Selected Successfully.");
//            System.out.println(selectedPolicy.getPolicyDetails());
        } catch (PolicyNotFoundException e) {
            System.out.println(e.getClass().getSimpleName() + " : " + e.getMessage());
        }
    }

    private void handleCalculatePremium() {
        if (selectedPolicy == null) {
            System.out.println("Please select a policy first.");
            return;
        }
        try {
            PremiumCalculable standardCalculator = new StandardPremiumCalculator();
            PremiumCalculable bonusCalculator = new NoClaimBonusCalculator();
            double standardPremium = standardCalculator.calculatePremium(selectedPolicy);
            double bonusPremium = bonusCalculator.calculatePremium(selectedPolicy);
            System.out.println("Standard Premium : " + standardPremium);
            System.out.println("No Claim Bonus Premium : " + bonusPremium);
        }
        catch (PolicyBusinessException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleExpirePolicy() {
        if (selectedPolicy == null) {
            System.out.println("Please select a policy first.");
            return;
        }
        try {
            selectedPolicy.expirePolicy();
            System.out.println("Policy expired successfully.");

            System.out.println("Current Status : " + selectedPolicy.getStatus());
        } catch (PolicyBusinessException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleRenewPolicy() {
        if (selectedPolicy == null) {
            System.out.println("Please select a policy first.");
            return;
        }
        try {
            selectedPolicy.renewPolicy(LocalDate.now());
            System.out.println("Policy renewed successfully.");
            System.out.println("Current Status : " + selectedPolicy.getStatus());

        } catch (PolicyBusinessException e) {
            System.out.println(e.getClass().getSimpleName() + " : " + e.getMessage());
        }
    }

    private void handleRecordClaim() {
        if (selectedPolicy == null) {
            System.out.println("Please select a policy first.");
            return;
        }
        try {
            selectedPolicy.recordClaim();
            System.out.println("Claim recorded successfully.");
            System.out.println("Total Claims : " + selectedPolicy.getClaimCount());

        } catch (PolicyBusinessException e) {
            System.out.println(e.getMessage());
        }
    }

    private void handleShowPolicyDetails() {

        if (selectedPolicy == null) {
            System.out.println("Please select a policy first.");
            return;
        }
        System.out.println();
        System.out.println(selectedPolicy.getPolicyDetails());
    }

    private void handleViewByCustomer() {

        try {
            String customerName = readString(scanner, "Enter Customer Name: ");
            List<Policy> customerPolicies = register.findByCustomer(customerName);

            if (customerPolicies.isEmpty()) {
                System.out.println("No policies found for customer " + customerName);
                return;
            }
            System.out.println();
            System.out.println("===== POLICIES FOR " + customerName.toUpperCase() + " =====");

            for (Policy policy : customerPolicies) {
                System.out.println(policy.getPolicyDetails());
            }
        } catch (PolicyBusinessException e) {
            System.out.println(e.getClass().getSimpleName() + " : " + e.getMessage());
        }
    }

    private void handleViewByVehicleType() {

        try {
            VehicleType vehicleType = readVehicleType(scanner);
            List<Policy> policies = register.findByVehicleType(vehicleType);

            if (policies.isEmpty()) {
                System.out.println("No policies found for " + vehicleType);
                return;
            }

            System.out.println();
            System.out.println("===== " + vehicleType + " POLICIES =====");

            for (Policy policy : policies) {
                System.out.println(policy.getPolicyDetails());
            }

        } catch (PolicyBusinessException e) {
            System.out.println(e.getClass().getSimpleName() + " : " + e.getMessage());
        }
    }

    private void handleViewExpiringSoon() {

        try {
            int days = readInt(scanner, "Enter number of days: ");
            List<Policy> policies = register.findExpiringWithin(days, LocalDate.now());

            if (policies.isEmpty()) {
                System.out.println("No policies expiring within " + days + " days.");
                return;
            }
            System.out.println();
            System.out.println("===== POLICIES EXPIRING WITHIN " + days + " DAYS =====");

            for (Policy policy : policies) {
                System.out.println(policy.getPolicyDetails());
            }
        } catch (PolicyBusinessException e) {
            System.out.println(e.getClass().getSimpleName() + " : " + e.getMessage());
        }
    }

    private void handleViewSummary() {

        try {
            Map<VehicleType, Double> summary = register.totalPremiumByVehicleType();
            if (summary.isEmpty()) {
                System.out.println("No policies available.");
                return;
            }
            System.out.println();
            System.out.println("===== PREMIUM SUMMARY =====");
            for (VehicleType vehicleType : summary.keySet()) {

                System.out.println(vehicleType + " : " + summary.get(vehicleType));
            }
        } catch (PolicyBusinessException e) {
            System.out.println(e.getClass().getSimpleName() + " : " + e.getMessage());
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

    private static double readDouble(Scanner scanner, String message) {
        while (true) {
            try {
                System.out.print(message);
                double value = Double.parseDouble(scanner.nextLine());
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be greater than 0.");

            } catch (NumberFormatException e) {
                throw new InvalidPolicyDataException("UNKNOWN", "Load Capacity must be a valid number.", e);
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

    private static int readCustomerAge(
            Scanner scanner,
            PolicyValidator validator) {

        while (true) {

            try {
                int age = readInt(scanner, "Enter customer age: ");
                validator.validateCustomerAge(age);
                return age;

            } catch (InvalidPolicyDataException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static String readCustomerName(Scanner scanner, PolicyValidator validator) {

        while (true) {
            try {

                String name = readString(scanner, "Enter customer name: ");
                validator.validateCustomerName(name);
                return name;

            } catch (InvalidPolicyDataException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static int readClaimCount(Scanner scanner, PolicyValidator validator, String policyNumber) {

        while (true) {
            try {
                int claimCount = readInt(scanner,"Enter previous claims count: ");
                validator.validateClaimCount(policyNumber, claimCount);
                return claimCount;

            } catch (InvalidPolicyDataException e) {

                System.out.println(e.getMessage());
            }
        }
    }

    private static LocalDate readStartDate(Scanner scanner, PolicyValidator validator, String policyNumber) {

        while (true) {
            try {
                LocalDate startDate = readDate(scanner,"Enter policy start date yyyy-mm-dd: ");
                int duration = readInt(scanner, "Enter policy duration in years: ");

                validator.validateExpiryDate(policyNumber, startDate, duration);
                return startDate;

            } catch (InvalidPolicyDataException e) {

                System.out.println(e.getMessage());
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