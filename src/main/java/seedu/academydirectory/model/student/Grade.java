package seedu.academydirectory.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.commons.util.AppUtil.checkArgument;

/**
 * Represents a Student's grade for an assessment in the Academy Directory.
 * Guarantees: immutable; is valid as declared in {@link #isValidGrade(String)}
 */
public class Grade implements Information {

    public static final String MESSAGE_CONSTRAINTS =
            "Grade should only contain non-negative integers, and it should range from 0 to 100.";
    public static final String VALIDATION_REGEX = "^(100|[1-9]?[0-9])$";
    private final String value;

    /**
     * Constructs a {@code Grade}.
     *
     * @param grade A valid grade.
     */
    public Grade(String grade) {
        requireNonNull(grade);
        checkArgument(isValidGrade(grade), MESSAGE_CONSTRAINTS);
        value = grade;
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidGrade(String test) {
        requireNonNull(test);
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Grade // instanceof handles nulls
                && value.equals(((Grade) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
