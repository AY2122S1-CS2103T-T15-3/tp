package seedu.academydirectory.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.academydirectory.logic.commands.CommandTestUtil.showStudentAtIndex;
import static seedu.academydirectory.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.academydirectory.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.academydirectory.testutil.TypicalStudents.getTypicalAcademyDirectory;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.commons.core.Messages;
import seedu.academydirectory.commons.core.index.Index;
import seedu.academydirectory.model.AcademyDirectory;
import seedu.academydirectory.model.UserPrefs;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.VersionedModelManager;
import seedu.academydirectory.model.student.Student;
import seedu.academydirectory.testutil.StudentBuilder;


/**
 * Contains integration tests (interaction with the VersionedModel) and unit tests for AttendanceCommand
 */
public class AttendanceCommandTest {

    private static final boolean STUDIO_ATTENDANCE_STUB = true;
    private static final Integer STUDIO_SESSION_STUB = 1;
    private static final Index INDEX_STUB = INDEX_FIRST_STUDENT;
    private static final ArrayList<Index> INDEX_ARRAYLIST_STUB = new ArrayList<>();
    private static final ArrayList<Index> INDEX_ARRAYLIST_STUB_2 = new ArrayList<>();
    private static final boolean[] BOOLEAN_ARRAY_STUB = new boolean[12];

    private VersionedModel model = new VersionedModelManager(getTypicalAcademyDirectory(), new UserPrefs());

    @Test
    public void execute_updateAttendance_success() {
        INDEX_ARRAYLIST_STUB.add(INDEX_STUB);
        BOOLEAN_ARRAY_STUB[0] = true;
        Student firstStudent = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        Student editedStudent = new StudentBuilder(firstStudent).withAttendance(BOOLEAN_ARRAY_STUB).build();

        AttendanceCommand addAttendanceCommand =
                new AttendanceCommand(STUDIO_ATTENDANCE_STUB, STUDIO_SESSION_STUB, INDEX_ARRAYLIST_STUB);
        String expectedMessage = AttendanceCommand.MESSAGE_UPDATE_ATTENDANCE_SUCCESS;

        VersionedModel expectedModel = new VersionedModelManager(
                new AcademyDirectory(model.getAcademyDirectory()), new UserPrefs());
        expectedModel.setStudent(firstStudent, editedStudent);
        assertCommandSuccess(addAttendanceCommand, model, expectedMessage, expectedModel);

        BOOLEAN_ARRAY_STUB[0] = false;
        Student otherEditedStudent = new StudentBuilder(firstStudent).withAttendance(BOOLEAN_ARRAY_STUB).build();
        expectedModel.setStudent(editedStudent, otherEditedStudent);
        AttendanceCommand removeAttendanceCommand =
                new AttendanceCommand(false, STUDIO_SESSION_STUB, INDEX_ARRAYLIST_STUB);
        assertCommandSuccess(removeAttendanceCommand, model, expectedMessage, expectedModel);

    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        ArrayList<Index> indexArrayListOutOfBounds = new ArrayList<>();
        indexArrayListOutOfBounds.add(outOfBoundIndex);
        AttendanceCommand attendanceCommand =
                new AttendanceCommand(STUDIO_ATTENDANCE_STUB, STUDIO_SESSION_STUB, indexArrayListOutOfBounds);
        assertCommandFailure(attendanceCommand, model, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }
    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of academy directory
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);
        Index outOfBoundIndex = INDEX_SECOND_STUDENT;
        // ensures that outOfBoundIndex is still in bounds of academy directory list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAcademyDirectory().getStudentList().size());
        ArrayList<Index> indexArrayListOutOfBounds = new ArrayList<>();
        indexArrayListOutOfBounds.add(outOfBoundIndex);
        AttendanceCommand attendanceCommand =
                new AttendanceCommand(STUDIO_ATTENDANCE_STUB, STUDIO_SESSION_STUB, indexArrayListOutOfBounds);

        assertCommandFailure(attendanceCommand, model, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        INDEX_ARRAYLIST_STUB.add(INDEX_FIRST_STUDENT);
        INDEX_ARRAYLIST_STUB_2.add(INDEX_SECOND_STUDENT);
        final AttendanceCommand standardCommand =
                new AttendanceCommand(STUDIO_ATTENDANCE_STUB, STUDIO_SESSION_STUB, INDEX_ARRAYLIST_STUB);

        // same values -> returns true
        AttendanceCommand commandWithSameValues =
                new AttendanceCommand(STUDIO_ATTENDANCE_STUB, STUDIO_SESSION_STUB, INDEX_ARRAYLIST_STUB);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different indexArrayList
        assertFalse(standardCommand.equals(
                new AttendanceCommand(STUDIO_ATTENDANCE_STUB, STUDIO_SESSION_STUB, INDEX_ARRAYLIST_STUB_2)));

        // different attendance
        assertFalse(standardCommand.equals(
                new AttendanceCommand(false, STUDIO_SESSION_STUB, INDEX_ARRAYLIST_STUB)));

        // different session
        assertFalse(standardCommand.equals(
                new AttendanceCommand(STUDIO_ATTENDANCE_STUB, 2, INDEX_ARRAYLIST_STUB)));
    }
}
