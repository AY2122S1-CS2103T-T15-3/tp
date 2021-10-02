package seedu.academydirectory.model.studio;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.commons.util.AppUtil.checkArgument;

public class StudioGroup {

    public static final String MESSAGE_CONSTRAINTS = "Studio Group names should be in the format"
            + "SGxx where xx is Studio Group number.\n"
            + "Studio Group should not be blank.";

    public static final String VALIDATION_REGEX = "(?<![\\w\\d])SG\\d+";

    public final String studioGroup;

    public StudioGroup(String studioGroup) {
        requireNonNull(studioGroup);
        checkArgument(isValidStudioGroup(studioGroup), MESSAGE_CONSTRAINTS);
        this.studioGroup = studioGroup;
    }

    public static boolean isValidStudioGroup(String test) {
        return test.matches(VALIDATION_REGEX);
    }

}
