package com.wipfli.insurancemanagement.main;

import com.wipfli.insurancemanagement.model.*;
import com.wipfli.insurancemanagement.service.*;
import com.wipfli.insurancemanagement.exception.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class MainApp {

    private static final LocalDate REFERENCE_DATE = LocalDate.of(2026, 8, 9);
    private final Scanner scanner = new Scanner(System.in);
    private final PolicyRegister register = new PolicyRegister();
    private final PolicyValidator validator = new PolicyValidator();
    private final PremiumCalculable premiumCalculator = new NoClaimBonusCalculator();
    private Policy selectedPolicy;



    public static void main(String[] args) {

        new MainApp().run();
    }

    public void run() {
        seedSampleData();

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
                    handleFindPolicyByNumberUsingStream();
                    break;

                case 8:
                    handleFindPoliciesByCustomerUsingStream();
                   
                    break;

                case 9:
                    handleTotalPremiumByVehicleTypeUsingStream();
                    break;
//new
                case 10:
                    handlePolicyCountByVehicleTypeUsingStream();
                    break;

                case 11:
                    handleExpiringPoliciesUsingStream();
                    break;

                case 12:
                    handleCustomerWithMostPoliciesUsingStream();
                    break;

                case 13:
                    handleTopFiveHighestPremiumPoliciesUsingStream();
                    break;
                case 14:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void seedSampleData() {
        try {
            PolicyOwner ravi = new PolicyOwner("Ravi", 22);
            PolicyOwner meena = new PolicyOwner("Meena", 34);
            PolicyOwner ajay = new PolicyOwner("Ajay", 41);
            PolicyOwner sneha = new PolicyOwner("Sneha", 29);

            register.add(new CarPolicy("POL-2001", ravi, VehicleType.CAR, 1, LocalDate.of(2026, 8, 15),1,"MH12AB9889"));
            register.add(new TruckPolicy("POL-2002", ravi, VehicleType.TRUCK, 0, LocalDate.of(2026, 9, 1),1,5000));
            register.add(new BikePolicy("POL-2003", meena, VehicleType.BIKE, 2, LocalDate.of(2026, 8, 20),1,500));
            register.add(new CarPolicy("POL-2004", ajay, VehicleType.CAR, 0, LocalDate.of(2027, 1, 10),1,"MH12AB7887"));
            register.add(new BikePolicy("POL-2005", sneha, VehicleType.BIKE, 0, LocalDate.of(2026, 8, 25),1,200));
            register.add(new CarPolicy("POL-2006", ravi, VehicleType.CAR, 3, LocalDate.of(2026, 12, 1),1,"MH12AB1235"));

        } catch (PolicyBusinessException exception) {
            System.out.println("Error while loading sample data: " + exception.getMessage());
        }
    }


    private void printMenu() {
        System.out.println("\n===== POLICY MANAGEMENT SYSTEM =====");

        System.out.println("1. Add Policy");
        System.out.println("2. Select Policy");
        System.out.println("3. Calculate Premium");
        System.out.println("4. Expire Policy");
        System.out.println("5. Renew Policy");
        System.out.println("6. Record Claim");
        System.out.println("7. Find Policy By Number Using Stream");
        System.out.println("8. Find Policies By Customer Using Stream");
        System.out.println("9. Total Premium By Vehicle Type Using Stream");
        System.out.println("10. Count Policies By Vehicle Type Using Stream");
        System.out.println("11. Find Policies Expiring Soon Using Stream");
        System.out.println("12. Find Customer With Most Policies Using Stream");
        System.out.println("13. Top 5 Highest Premium Policies Using Stream");
        System.out.println("14. Exit");
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
            selectedPolicy = register.findByPolicyNumber(policyNumber).orElseThrow(() -> new PolicyNotFoundException("Policy not found: " + policyNumber));
            System.out.println("Policy Selected Successfully.");
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

    private void handleFindPolicyByNumberUsingStream() {

            String policyNumber = readString(scanner,"Enter Policy Number: ");
            Optional<Policy> optionalPolicy = register.findByPolicyNumber(policyNumber);
            if(optionalPolicy.isPresent()){
                System.out.println("Policy found: " + optionalPolicy.get().getPolicyDetails());
            }
            else{
                System.out.println("No policy found with number: "+ policyNumber);

            }
    }

    private void handleFindPoliciesByCustomerUsingStream() {
        try {
            String customerName = readString(scanner,"Enter Customer Name: ");

            List<Policy> policies = register.findPoliciesByCustomerName(customerName);

            if (policies.isEmpty()) {
                System.out.println("No policies found for customer: " + customerName);
                return;
            }
            System.out.println("Policies for customer: " + customerName);
            policies.forEach(System.out::println);

        } catch (Exception exception) {
            System.out.println("Error while finding customer policies: " + exception.getMessage());
        }
    }

    private void handleTotalPremiumByVehicleTypeUsingStream() {
        try {

            System.out.println("\nTotal Premium By Vehicle Type");

            for (VehicleType vehicleType : VehicleType.values()) {

                double totalPremium = register.calculateTotalPremiumByVehicleType(vehicleType,premiumCalculator);

                System.out.println(vehicleType + " : " + totalPremium);
            }
        } catch (Exception exception) {
            System.out.println("Error while calculating total premium: "
                    + exception.getMessage());
        }
    }

    private void handlePolicyCountByVehicleTypeUsingStream() {
        try {
            Map<VehicleType, Long> policyCountMap = register.countPoliciesByVehicleType();

            if (policyCountMap.isEmpty()) {
                System.out.println("No policies available.");
                return;
            }
            System.out.println("Policy count by vehicle type:");

            policyCountMap.forEach((vehicleType, count) -> System.out.println(vehicleType + " : " + count));

        } catch (Exception exception) {
            System.out.println("Error while counting policies: " + exception.getMessage());
        }
    }
    private void handleExpiringPoliciesUsingStream() {
        try {
            int days = readInt(scanner,"Enter number of days: ");

            List<Policy> expiringPolicies =
                    register.findPoliciesExpiringWithin(days, REFERENCE_DATE);

            if (expiringPolicies.isEmpty()) {
                System.out.println("No policies expiring within " + days + " days.");
                return;
            }

            System.out.println("Policies expiring within " + days + " days from " + REFERENCE_DATE + ":");
            expiringPolicies.forEach(System.out::println);

        } catch (Exception exception) {
            System.out.println("Error while finding expiring policies: " + exception.getMessage());
        }
    }

    private void handleCustomerWithMostPoliciesUsingStream() {
        try {
            String result = register.findCustomerWithMostPoliciesUsingStream()
                    .map(customerName -> "Customer with most policies: " + customerName + " with a policy of " + register.findPoliciesByCustomerName(customerName).size())
                    .orElse("No policies available.");

            System.out.println(result);

        } catch (Exception exception) {
            System.out.println("Error while finding customer with most policies: " + exception.getMessage());
        }
    }

    private void handleTopFiveHighestPremiumPoliciesUsingStream() {
        try {
            List<Policy> topPolicies = register.findTopFiveHighestPremiumPoliciesUsingStream(premiumCalculator);

            if (topPolicies.isEmpty()) {
                System.out.println("No policies available.");
                return;
            }
            System.out.println("Top 5 highest premium policies:");

            topPolicies.forEach(policy -> {
                double premium = premiumCalculator.calculatePremium(policy);
                System.out.println("Policy Number: " + policy.getPolicyNumber()
                                + " | Customer: " + policy.getPolicyOwner().getName()
                                + " | Vehicle Type: " + policy.getVehicleType()
                                + " | Premium: " + premium
                );
            });

        } catch (Exception exception) {
            System.out.println("Error while finding top premium policies: " + exception.getMessage());
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