package registerpeople;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Scanner;

public class Person extends Entity implements Recordable {

    private String lastName;
    private LocalDate dob;

    @Override
    public void inputDetails(Scanner scanner) {
        readName(scanner, "Enter First Name: ");
        readLastName(scanner);
        readDob(scanner);
        readJurisdiction(scanner);
    }

    private void setLastName(String lastName) {
        Validator.validateName(lastName, "Last Name");
        this.lastName = lastName.trim();
    }

    private void setDob(LocalDate dob) {
        Validator.validateDob(dob);
        this.dob = dob;
    }

    private void readLastName(Scanner scanner) {
        while (true) {
            System.out.print("Enter Last Name: ");
            String input = scanner.nextLine();
            try {
                setLastName(input);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void readDob(Scanner scanner) {
        while (true) {
            System.out.print("Enter DOB (dd/MM/yyyy): ");
            String input = scanner.nextLine();

            try {
                LocalDate parsed = LocalDate.parse(input, Constants.DATE_FORMATTER);
                setDob(parsed);
                break;

            } catch (Exception e) {
                System.out.println("Invalid DOB!");
            }
        }
    }

    public String getFullName() {
        return getName() + " " + lastName;
    }

    public int getAge() {
        return Period.between(dob, LocalDate.now()).getYears();
    }

    @Override
    public void display() {
        System.out.println("\n--- Person ---");
        System.out.println("Name: " + getFullName());
        System.out.println("DOB: " + dob.format(Constants.DATE_FORMATTER));
        System.out.println("Age: " + getAge());
        System.out.println("Jurisdiction: " + getJurisdictionFullName());
    }

    @Override
    public void saveToFile() {
        List<String> lines = List.of(
                "Type: Person", 
                "Name: " + getFullName(),
                "DOB: " + dob.format(Constants.DATE_FORMATTER),
                "Jurisdiction: " + getJurisdiction().name(),
                "---------------------"
        );

        FileManager.writeToFile(lines);
    }
}