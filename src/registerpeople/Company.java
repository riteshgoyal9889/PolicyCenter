package registerpeople;

import java.util.List;
import java.util.Scanner;

public class Company extends Entity implements Recordable {

    @Override
    public void inputDetails(Scanner scanner) {
        readName(scanner, "Enter Company Name: ");
        readJurisdiction(scanner);
    }

    @Override
    public void display() {
        System.out.println("\n--- Company ---");
        System.out.println("Name: " + getName());
        System.out.println("Jurisdiction: " + getJurisdictionFullName());
    }

    @Override
    public void saveToFile() {
        List<String> lines = List.of(
                "Type: Company",
                "Company Name: " + getName(),
                "Jurisdiction: " + getJurisdiction().name(),
                "---------------------"
        );

        FileManager.writeToFile(lines);
    }
}
