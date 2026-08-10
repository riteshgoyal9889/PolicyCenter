package com.wipfli.insurancemanagement.main;

import com.wipfli.insurancemanagement.model.*;
import com.wipfli.insurancemanagement.service.*;
import com.wipfli.insurancemanagement.exception.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        List<Policy> policies = new ArrayList<>();
        PolicyRegister register = new PolicyRegister();
        PolicyValidator validator = new PolicyValidator();

        System.out.println("===== Insurance Management System =====");
        int policyCount = readInt(scanner, "Enter number of policies: ");

        for (int i = 1; i <= policyCount; i++) {
            System.out.println();
            System.out.println("Enter details for policy " + i);

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
                policies.add(policy);

                System.out.println("Policy added successfully.");
                System.out.println();


            } catch (PolicyBusinessException e) {

                System.out.println(e.getClass().getSimpleName() + " : " + e.getMessage());

                System.out.println("Please re-enter this policy.");
                i--;
            }

        }

        System.out.println();

        showPolicyMenu(scanner, register);

        scanner.close();
    }

    private static void showPolicyMenu(Scanner scanner,  PolicyRegister register) {

        PremiumCalculable standardCalculator = new StandardPremiumCalculator();
        PremiumCalculable noClaimCalculator = new NoClaimBonusCalculator();
        Policy selectedPolicy = null;
        while (true) {

            System.out.println("\n===== POLICY MENU =====");
            System.out.println("1. Select Policy to Perform function");
            System.out.println("2. Calculate Premium");
            System.out.println("3. Expire Policy");
            System.out.println("4. Renew Policy");
            System.out.println("5. Record Claim");
            System.out.println("6. Show Policy Details");
            System.out.println("7. Exit");

            int choice = readInt(scanner, "Enter choice: ");

            try {
                switch (choice) {
                    case 1:

                        try {

                            String searchPolicyNumber = readString(scanner,"Enter Policy Number: ");
                            selectedPolicy = register.findByNumber(searchPolicyNumber);
                            System.out.println("Policy Selected Successfully.");

                        } catch (PolicyNotFoundException e) {

                            System.out.println(e.getClass().getSimpleName()
                                            + " : " + e.getMessage());
                        }

                        break;
                    case 2:
                        if (selectedPolicy == null) {
                            System.out.println("Please select a policy first.");
                            break;
                        }
                        double standardPremium = standardCalculator.calculatePremium(selectedPolicy);
                        double bonusPremium = noClaimCalculator.calculatePremium(selectedPolicy);
                        System.out.println("\nStandard Premium : " + standardPremium);
                        System.out.println("No Claim Bonus Premium : " + bonusPremium);
                        break;

                    case 3:
                        if (selectedPolicy == null) {
                            System.out.println("Please select a policy first.");
                            break;
                        }
                        selectedPolicy.expirePolicy();
                        System.out.println("Policy expired successfully.");
                        System.out.println("Current Status: " + selectedPolicy.getStatus());
                        break;

                    case 4:
                        if (selectedPolicy == null) {
                            System.out.println("Please select a policy first.");
                            break;
                        }
                        System.out.println("Expiry Date : " + selectedPolicy.getExpiryDate());
                        selectedPolicy.renewPolicy(LocalDate.now());
                        System.out.println("Policy renewed successfully.");
                        System.out.println("Current Status: " + selectedPolicy.getStatus());
                        break;

                    case 5:
                        if (selectedPolicy == null) {
                            System.out.println("Please select a policy first.");
                            break;
                        }
                        selectedPolicy.recordClaim();
                        System.out.println("Claim recorded successfully.");
                        System.out.println("Total Claims : " + selectedPolicy.getClaimCount());
                        break;

                    case 6:
                        if (selectedPolicy == null) {
                            System.out.println("Please select a policy first.");
                            break;
                        }
                        System.out.println();
                        System.out.println(selectedPolicy.getPolicyDetails());
                        break;

                    case 7:
                        return;

                    default:
                        System.out.println("Invalid choice.");
                }

            }catch (PolicyBusinessException e) {

                System.out.println(e.getClass().getSimpleName() + " : "     + e.getMessage());
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