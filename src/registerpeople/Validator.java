package registerpeople;

import java.time.LocalDate;

public final class Validator {

    private Validator() {}

    public static void validateName(String name, String field) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(field + " cannot be empty.");
        }

        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                throw new IllegalArgumentException(
                        field + " must contain only letters and spaces."
                );
            }
        }
    }

    public static void validateDob(LocalDate dob) {
        if (dob == null || dob.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid DOB.");
        }
    }

    public static void validateJurisdiction(Jurisdiction jurisdiction) {
        if (jurisdiction == null) {
            throw new IllegalArgumentException("Jurisdiction cannot be null.");
        }
    }
}