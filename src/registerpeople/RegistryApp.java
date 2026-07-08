package registerpeople;

import java.util.Scanner;

public class RegistryApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            showMenu();
            int choice = readChoice(scanner);
            switch (choice) {
                case 1:
                    process(new Person(), scanner);
                    break;
                case 2:
                    FileManager.readFromFile();
                    break;
                case 3:
                    process(new Company(), scanner);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
    private static void showMenu() {
        System.out.println("\n===== MENU =====");
        System.out.println("1. Add Person");
        System.out.println("2. View Data");
        System.out.println("3. Add Company");
        System.out.println("4. Exit");
    }
    private static int readChoice(Scanner scanner) {
        while (true) {
            System.out.print("Choice: ");
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input!");
            }
        }
    }
    private static <T extends Entity & Recordable> void process(T obj, Scanner sc) {
        obj.inputDetails(sc);
        obj.display();
        obj.saveToFile();
    }
}
