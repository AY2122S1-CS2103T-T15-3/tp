package seedu.academydirectory.model.studio;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.commons.util.AppUtil.checkArgument;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StudioSlot {

    public static final String MESSAGE_CONSTRAINTS = "Studio Slot names should be in the format\n"
            + "<STARTING DATE>/<TIME>. where STARTING DATE is the date of the first or next Studio"
            + " and the TIME is in the 24H format e.g '16:30', 00:30' etc\n"
            + "Studio Slot should not be blank.";

    // TODO: for future use when printing date of next Studio
    private static final String[] days = new String[] {"Mon", "Tues", "Wed", "Thur", "Fri", "Sat", "Sun"};

    public final String time;
    public final DayOfWeek day;
    private final LocalDateTime studioSlot;

    public StudioSlot(LocalDateTime studioSlot) {
        requireNonNull(studioSlot);
        checkArgument(isValidStudioSlot(studioSlot), MESSAGE_CONSTRAINTS);
        this.studioSlot = studioSlot;
        this.time = setTime(studioSlot);
        this.day = studioSlot.getDayOfWeek();
    }

    public static boolean isValidStudioSlot(LocalDateTime testDate) {
        return testDate != null;
    }

    public static LocalDateTime stringToDateTime(String studioSlot) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String studioSlotWithoutBackslash = studioSlot.replace('/', ' ');
        try {
            return LocalDateTime.parse(studioSlotWithoutBackslash, formatter);
        } catch (DateTimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String setTime(LocalDateTime parsedSlot) {
        int minute = parsedSlot.getMinute();
        int hour = parsedSlot.getHour();
        String minuteStr = minute < 10 ? "0" + minute : minute + "";
        String hourStr = hour < 10 ? "0" + hour : hour + "";
        return hourStr + ":" + minuteStr;
    }

}
