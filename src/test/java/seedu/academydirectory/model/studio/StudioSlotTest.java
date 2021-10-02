package seedu.academydirectory.model.studio;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.academydirectory.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class StudioSlotTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StudioSlot(null));
    }

    @Test
    public void isValidStudioSlot() {
        assertFalse(StudioSlot.isValidStudioSlot(StudioSlot.stringToDateTime("")));
        assertFalse(StudioSlot.isValidStudioSlot(StudioSlot.stringToDateTime(" ")));
        assertFalse(StudioSlot.isValidStudioSlot(StudioSlot.stringToDateTime("43-12-2012/22:23")));
        assertFalse(StudioSlot.isValidStudioSlot(StudioSlot.stringToDateTime("13-12-2012/24:23")));

        assertTrue(StudioSlot.isValidStudioSlot(StudioSlot.stringToDateTime("12-12-2012/12:23")));
        assertTrue(StudioSlot.isValidStudioSlot(StudioSlot.stringToDateTime("12-12-2012/02:23")));
    }

    @Test
    public void test_setTime_setTheCorrectTime() {

        LocalDateTime testDate1 = LocalDateTime.of(1999, 12, 12, 12, 22);
        assertTrue(StudioSlot.setTime(testDate1).equals("12:22"));

        LocalDateTime testDate2 = LocalDateTime.of(1999, 12, 12, 02, 00);
        assertTrue(StudioSlot.setTime(testDate2).equals("02:00"));

        LocalDateTime testDate3 = LocalDateTime.of(1999, 12, 12, 12, 9);
        assertTrue(StudioSlot.setTime(testDate3).equals("12:09"));
    }

}
