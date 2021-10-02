package seedu.academydirectory.model.studio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.academydirectory.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;
import seedu.academydirectory.model.person.Address;

public class StudioGroupTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StudioGroup(null));
    }

    @Test
    public void constructor_invalidStudioGroup_throwsIllegalArgumentException() {
        String invalidAddress = "";
        assertThrows(IllegalArgumentException.class, () -> new Address(invalidAddress));
    }

}