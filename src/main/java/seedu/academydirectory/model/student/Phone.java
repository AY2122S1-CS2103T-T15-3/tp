package seedu.academydirectory.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.commons.util.AppUtil.checkArgument;

import java.util.Optional;

/**
 * Represents a Student's phone number in the academy directory.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone implements PersonalDetail {


    public static final String MESSAGE_CONSTRAINTS =
            "Phone numbers should only contain numbers, and it should be at least 3 digits long"
            + " or 'NA' if you don't want to specify phone number";
    public static final String PHONE_REGEX = "\\d{3,}";
    public static final String NA_REGEX = "^NA$";
    public static final String VALIDATION_REGEX = PHONE_REGEX + "|" + NA_REGEX;
    public final Optional<String> value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        checkArgument(isValidPhone(phone), MESSAGE_CONSTRAINTS);
        if (isEmpty(phone)) {
            value = Optional.empty();
        } else {
            value = Optional.of(phone);
        }
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    private static boolean isEmpty(String test) {
        return test.matches(NA_REGEX);
    }

    @Override
    public String toString() {
        return value.orElse("NA");
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Phone // instanceof handles nulls
                && this.toString().equals(other.toString())); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
