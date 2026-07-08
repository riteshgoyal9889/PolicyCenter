package registerpeople;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public final class FileManager {

    private static final Path FILE_PATH =
            Paths.get(Constants.FILE_NAME);

    private FileManager() {}

    public static void writeToFile(List<String> lines) {
        try {
            Files.write(FILE_PATH, lines,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
            System.out.println("Saved successfully!");
        } catch (IOException e) {
            System.out.println("Error writing file");
        }
    }

    public static void readFromFile() {
        try {
            if (!Files.exists(FILE_PATH)) {
                System.out.println("No data found!");
                return;
            }

            List<String> lines = Files.readAllLines(FILE_PATH);

            for (String line : lines) {

                if (line.startsWith("DOB: ")) {
                    String dobStr = line.substring(5);

                    try {
                        LocalDate dob =LocalDate.parse(dobStr,Constants.DATE_FORMATTER);

                        int age = Period.between(dob, LocalDate.now()).getYears();

                        System.out.println(line);
                        System.out.println("Age: " + age);

                    } catch (Exception e) {
                        System.out.println(line);
                    }

                } else if (line.startsWith("Jurisdiction: ")) {
                    String code = line.substring(14);

                    try {
                        System.out.println("Jurisdiction: " +Jurisdiction.valueOf(code).getFullName());
                    } catch (Exception e) {
                        System.out.println("Jurisdiction: Unknown");
                    }

                } else {
                    System.out.println(line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file");
        }
    }
}