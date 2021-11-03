package seedu.academydirectory.model.student;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.commons.util.AppUtil.checkArgument;

/**
 * Represents a Student's Telegram handle in the academy directory.
 * Guarantees: immutable; is valid as declared in {@link #isValidTelegram(String)}
 */
public class Telegram implements PersonalDetail {

    private static final String SPECIAL_CHARACTERS = "+_.-";
    public static final String MESSAGE_CONSTRAINTS = "Telegram handles should be of the format @handle "
            + "and adhere to the following constraint:\n"
            + "The handle should only contain alphanumeric characters and these special characters, excluding "
            + "the parentheses, (" + SPECIAL_CHARACTERS + "). The handle may not start or end with any special "
            + "characters.";
    // alphanumeric and special characters
    private static final String ALPHANUMERIC_NO_UNDERSCORE = "[^\\W_]+"; // alphanumeric characters except underscore
    private static final String HANDLE_REGEX = ALPHANUMERIC_NO_UNDERSCORE + "([" + SPECIAL_CHARACTERS + "]"
            + ALPHANUMERIC_NO_UNDERSCORE + ")*";
    public static final String VALIDATION_REGEX = "^" + "@" + HANDLE_REGEX;

    public final String value;

    /**
     * Constructs an {@code Telegram}.
     *
     * @param telegram A valid telegram handle.
     */
    public Telegram(String telegram) {
        requireNonNull(telegram);
        checkArgument(isValidTelegram(telegram), MESSAGE_CONSTRAINTS);
        value = telegram;
    }

    /**
     * Returns if a given string is a valid telegram.
     */
    public static boolean isValidTelegram(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Telegram // instanceof handles nulls
                && value.equals(((Telegram) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
