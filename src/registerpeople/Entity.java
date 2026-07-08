package registerpeople;

import java.util.Scanner;

public abstract class Entity {

    private String name;
    private Jurisdiction jurisdiction;

    public abstract void inputDetails(Scanner scanner);
    public abstract void display();

    protected void setName(String name) {
        Validator.validateName(name, "Name");
        this.name = name.trim();
    }

    protected String getName() {
        return name;
    }

    protected void setJurisdiction(Jurisdiction jurisdiction) {
        Validator.validateJurisdiction(jurisdiction);
        this.jurisdiction = jurisdiction;
    }

    protected Jurisdiction getJurisdiction() {
        return jurisdiction;
    }

    protected String getJurisdictionFullName() {
        return jurisdiction.getFullName();
    }

    protected void readName(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                setName(input);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    protected void readJurisdiction(Scanner scanner) {
        while (true) {
            System.out.print("Enter Jurisdiction (IL, IN, MN): ");
            String input = scanner.nextLine().toUpperCase();

            try {
                setJurisdiction(Jurisdiction.valueOf(input));
                break;
            } catch (Exception e) {
                System.out.println("Invalid jurisdiction!");
            }
        }
    }
}