package seedu.academydirectory.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.academydirectory.testutil.Assert.assertThrows;
import static seedu.academydirectory.testutil.TypicalStudents.getTypicalAcademyDirectory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.model.AcademyDirectory;
import seedu.academydirectory.model.UserPrefs;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.VersionedModelManager;
import seedu.academydirectory.model.student.Assessment;
import seedu.academydirectory.model.student.Email;
import seedu.academydirectory.model.student.Name;
import seedu.academydirectory.model.student.Phone;
import seedu.academydirectory.model.student.Student;
import seedu.academydirectory.model.student.StudioRecord;
import seedu.academydirectory.model.student.Telegram;

public class SortCommandTest {
    private final VersionedModel model = new VersionedModelManager(getTypicalAcademyDirectory(), new UserPrefs());
    private final Student student1 = new Student(
            new Name("Kesha"),
            new Phone("911"),
            new Email("kesha@gmail.com"),
            new Telegram("@Lesha"),
            new StudioRecord(12),
            new Assessment(),
            new HashSet<>());
    private final Student student2 = new Student(
            new Name("Lesha"),
            new Phone("811"),
            new Email("kesha@gmail.com"),
            new Telegram("@Lesha"),
            new StudioRecord(12),
            new Assessment(),
            new HashSet<>());
    private final Student student3 = new Student(
            new Name("Mesha"),
            new Phone("711"),
            new Email("mesha@gmail.com"),
            new Telegram("@mesha"),
            new StudioRecord(12),
            new Assessment(),
            new HashSet<>());

    @Test
    public void constructor_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SortCommand(null, true));
        assertThrows(NullPointerException.class, () -> new SortCommand(null, false));
    }

    @Test
    public void execute_sortStudentList_success() {

        // first for name
        student1.getAssessment().updateAssessmentGrade("RA1", 12); // last for RA1
        student1.getParticipation().add(10, 6); // middle for participation

        // middle for name
        student2.getAssessment().updateAssessmentGrade("RA1", 1); // first for RA1
        student2.getParticipation().add(10, 12); // last for participation

        // last for name
        student3.getAssessment().updateAssessmentGrade("RA1", 6); // middle for RA1
        student3.getParticipation().add(10, 1); // first for participation

        VersionedModel expectedModel = new VersionedModelManager(new AcademyDirectory(), new UserPrefs());
        AcademyDirectory expectedAcademyDirectory = new AcademyDirectory();
        List<Student> expectedStudentList = Arrays.asList(student1, student2, student3);
        AcademyDirectory modelAcademyDireory = new AcademyDirectory();
        List<Student> newList = new ArrayList<>(expectedStudentList);
        modelAcademyDireory.setStudents(new ArrayList<>(newList));
        model.setAcademyDirectory(modelAcademyDireory);

        SortCommand sortParticipationAscendingCommand =
                new SortCommand("PARTICIPATION", true);
        SortCommand sortParticipationDescendingCommand =
                new SortCommand("PARTICIPATION", false);

        // sort by ascending participation
        expectedStudentList = Arrays.asList(student3, student1, student2);

        expectedAcademyDirectory.setStudents(expectedStudentList);
        expectedModel.setAcademyDirectory(expectedAcademyDirectory);

        // before command executed
        assertNotEquals(expectedStudentList, model.getFilteredStudentList());
        assertCommandSuccess(sortParticipationAscendingCommand, model,
                sortParticipationAscendingCommand.getResultString(), expectedModel);

        //after command execute
        assertEquals(expectedStudentList, model.getFilteredStudentList());

        // sort by descending participation
        expectedStudentList = Arrays.asList(student2, student1, student3);

        expectedAcademyDirectory.setStudents(expectedStudentList);
        expectedModel.setAcademyDirectory(expectedAcademyDirectory);

        // before command executed
        assertNotEquals(expectedStudentList, model.getFilteredStudentList());

        //after command execute
        assertCommandSuccess(sortParticipationDescendingCommand, model,
                sortParticipationDescendingCommand.getResultString(), expectedModel);
        assertEquals(expectedStudentList, model.getFilteredStudentList());

        SortCommand sortNameAscendingCommand = new SortCommand("NAME", true);
        SortCommand sortNameDescendingCommand = new SortCommand("NAME", false);

        // sort by ascending name
        expectedStudentList = Arrays.asList(student1, student2, student3);

        expectedAcademyDirectory.setStudents(expectedStudentList);
        expectedModel.setAcademyDirectory(expectedAcademyDirectory);

        // before command executed
        assertNotEquals(expectedStudentList, model.getFilteredStudentList());

        // after command execute
        assertCommandSuccess(sortNameAscendingCommand, model,
                sortNameAscendingCommand.getResultString(), expectedModel);
        assertEquals(expectedStudentList, model.getFilteredStudentList());

        // sort by descending name
        expectedStudentList = Arrays.asList(student3, student2, student1);

        expectedAcademyDirectory.setStudents(expectedStudentList);
        expectedModel.setAcademyDirectory(expectedAcademyDirectory);

        // before command executed
        assertNotEquals(expectedStudentList, model.getFilteredStudentList());

        //after command execute
        assertCommandSuccess(sortNameDescendingCommand, model,
                sortNameDescendingCommand.getResultString(), expectedModel);
        assertEquals(expectedStudentList, model.getFilteredStudentList());

        SortCommand sortAverageAscendingCommand =
                new SortCommand("AVERAGE", true);
        SortCommand sortAverageDescendingCommand =
                new SortCommand("AVERAGE", false);

        // sort by ascending average
        expectedStudentList = Arrays.asList(student2, student3, student1);

        expectedAcademyDirectory.setStudents(expectedStudentList);
        expectedModel.setAcademyDirectory(expectedAcademyDirectory);

        // before command executed
        assertNotEquals(expectedStudentList, model.getFilteredStudentList());

        //after command execute
        assertCommandSuccess(sortAverageAscendingCommand, model,
                sortAverageAscendingCommand.getResultString(), expectedModel);
        assertEquals(expectedStudentList, model.getFilteredStudentList());

        // sort by descending average
        expectedStudentList = Arrays.asList(student1, student3, student2);

        expectedAcademyDirectory.setStudents(expectedStudentList);
        expectedModel.setAcademyDirectory(expectedAcademyDirectory);

        // before command executed
        assertNotEquals(expectedStudentList, model.getFilteredStudentList());

        //after command execute
        assertCommandSuccess(sortAverageDescendingCommand, model,
                sortAverageDescendingCommand.getResultString(), expectedModel);
        assertEquals(expectedStudentList, model.getFilteredStudentList());

        SortCommand sortRA1AscendingCommand =
                new SortCommand("RA1", true);
        SortCommand sortRA1DescendingCommand =
                new SortCommand("RA1", false);

        // sort by ascending RA1 score
        expectedStudentList = Arrays.asList(student2, student3, student1);

        expectedAcademyDirectory.setStudents(expectedStudentList);
        expectedModel.setAcademyDirectory(expectedAcademyDirectory);

        // before command executed
        assertNotEquals(expectedStudentList, model.getFilteredStudentList());

        //after command execute
        assertCommandSuccess(sortRA1AscendingCommand, model,
                sortRA1AscendingCommand.getResultString(), expectedModel);
        assertEquals(expectedStudentList, model.getFilteredStudentList());

        // sort by descending RA1 score
        expectedStudentList = Arrays.asList(student1, student3, student2);

        expectedAcademyDirectory.setStudents(expectedStudentList);
        expectedModel.setAcademyDirectory(expectedAcademyDirectory);

        // before command executed
        assertNotEquals(expectedStudentList, model.getFilteredStudentList());

        //after command execute
        assertCommandSuccess(sortRA1DescendingCommand, model,
                sortRA1DescendingCommand.getResultString(), expectedModel);
        assertEquals(expectedStudentList, model.getFilteredStudentList());

    }

    @Test
    public void equals() {

        // ascending name command
        SortCommand sortAscendingNameCommand = new SortCommand("NAME", true);

        // equals to itself
        assertEquals(sortAscendingNameCommand, sortAscendingNameCommand);

        // duplicate ascending name command
        SortCommand anotherSortAscendingNameCommand = new SortCommand("NAME", true);

        // duplicate ascending name command equals to original
        assertEquals(sortAscendingNameCommand, anotherSortAscendingNameCommand);

        // duplicate ascending name command, but with different casing
        SortCommand anotherLowercaseSortAscendingNameCommand = new SortCommand("name", true);

        //duplicate ascending name command with lower case equals to original
        assertEquals(sortAscendingNameCommand, anotherLowercaseSortAscendingNameCommand);

        // descending name command
        SortCommand sortDescendingNameCommand = new SortCommand("NAME", false);

        // different order not equals
        assertNotEquals(sortAscendingNameCommand, sortDescendingNameCommand);

        // ascending RA1 command
        SortCommand sortAscendingRA1Command = new SortCommand("RA1", true);

        // different attribute not equals
        assertNotEquals(sortAscendingNameCommand, sortAscendingRA1Command);

        // different attribute, different order not equals
        assertNotEquals(sortDescendingNameCommand, sortAscendingRA1Command);
    }

    public void getComparator() {
        // name sort command
        SortCommand sortNameCommand = new SortCommand("NAME", true);

        // duplicate name sort command
        SortCommand duplicateSortNameCommand = new SortCommand("NAME", true);

        // participation sort command
        SortCommand sortParticipationCommand = new SortCommand("PARTICIPATION", true);

        // average sort command
        SortCommand sortAverageCommand = new SortCommand("AVERAGE", true);

        // RA1 sort command
        SortCommand sortRA1Command = new SortCommand("RA1", true);

        // comparator for sorting name
        Comparator<Student> sortNameComparator = sortNameCommand.getComparator();

        // duplicte comparator for sorting name
        Comparator<Student> duplicateSortNameComparator = duplicateSortNameCommand.getComparator();

        // comparator for sorting participation
        Comparator<Student> sortParticipationComparator = sortParticipationCommand.getComparator();

        // comparator for sorting average
        Comparator<Student> sortAverageComparator = sortAverageCommand.getComparator();

        // comparator for sorting RA1
        Comparator<Student> sortRA1Comparator = sortRA1Command.getComparator();

        // comparators for same attribute (name) are equal
        assertEquals(sortNameComparator, duplicateSortNameComparator);

        // comparator not equal to other comparators from different attributes
        assertNotEquals(sortNameComparator, sortAverageComparator);
        assertNotEquals(sortNameComparator, sortParticipationComparator);
        assertNotEquals(sortNameComparator, sortRA1Comparator);

    }
}
