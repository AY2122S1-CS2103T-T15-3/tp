package seedu.academydirectory.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.commons.util.AppUtil.checkArgument;

import java.util.Comparator;

/**
 * Represents a Student's name in the academy directory.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name extends SortableInformation implements PersonalDetail {

    public static final String MESSAGE_CONSTRAINTS =
            "Names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the name must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Name // instanceof handles nulls
                && fullName.equals(((Name) other).fullName)); // state check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

    public static Comparator<Student> getComparator(boolean isAscending) {
        return new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                Name name1 = s1.getName();
                Name name2 = s2.getName();
                return isAscending
                        ? name1.toString().compareTo(name2.toString())
                        : name2.toString().compareTo(name1.toString());
            }
        };
    }
}
