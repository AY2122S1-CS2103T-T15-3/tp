package seedu.academydirectory.model.studio;

import seedu.academydirectory.model.person.Person;

import java.util.HashSet;
import java.util.Set;

public class Studio {

    private final StudioGroup studioGroup;
    private final StudioSlot studioSlot;
    private final Set<Person> students = new HashSet<>();

    public Studio(StudioGroup studioGroup, StudioSlot studioSlot, Set<Person> students) {
        this.studioGroup = studioGroup;
        this.studioSlot = studioSlot;
        this.students.addAll(students);
    }
}
