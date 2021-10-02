package seedu.academydirectory.model.studio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.academydirectory.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class StudioGroupTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StudioGroup(null));
    }

    @Test
    public void constructor_invalidStudioGroup_throwsIllegalArgumentException() {
        String invalidStudioGroup = "";
        assertThrows(IllegalArgumentException.class, () -> new StudioGroup(invalidStudioGroup));
    }

    @Test
    public void isValidStudioGroup() {
        assertFalse(StudioGroup.isValidStudioGroup(""));
        assertFalse(StudioGroup.isValidStudioGroup(" "));

        assertFalse(StudioGroup.isValidStudioGroup("GS1"));
        assertFalse(StudioGroup.isValidStudioGroup("SGD1"));
        assertFalse(StudioGroup.isValidStudioGroup("sg1"));
        assertFalse(StudioGroup.isValidStudioGroup("SGe1"));

        assertTrue(StudioGroup.isValidStudioGroup("SG1454"));
        assertTrue(StudioGroup.isValidStudioGroup("SG1"));
        assertTrue(StudioGroup.isValidStudioGroup("SG12"));
        assertTrue(StudioGroup.isValidStudioGroup("SG13"));
    }

}
