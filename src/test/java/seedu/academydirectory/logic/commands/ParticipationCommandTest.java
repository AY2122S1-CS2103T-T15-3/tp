package seedu.academydirectory.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandSuccess;
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
import seedu.academydirectory.model.student.Attendance;
import seedu.academydirectory.model.student.Participation;
import seedu.academydirectory.model.student.Student;
import seedu.academydirectory.testutil.StudentBuilder;
import seedu.academydirectory.testutil.TypicalStudents;

/**
 * Contains integration tests (interaction with the VersionedModel) and unit tests for ParticipationCommand
 */
public class ParticipationCommandTest {

    private static final Integer STUDIO_SESSION_STUB = 1;
    private static final ArrayList<Index> INDEX_ARRAYLIST_STUB = new ArrayList<>();
    private static final ArrayList<Index> INDEX_ARRAYLIST_STUB_2 = new ArrayList<>();
    private VersionedModel model = new VersionedModelManager(getTypicalAcademyDirectory(), new UserPrefs());

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        ArrayList<Index> indexArrayListOutOfBounds = new ArrayList<>();
        indexArrayListOutOfBounds.add(outOfBoundIndex);
        ParticipationCommand participationCommand =
                new ParticipationCommand(12, STUDIO_SESSION_STUB, indexArrayListOutOfBounds);
        assertCommandFailure(participationCommand, model, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_updateParticipation_success() {
        INDEX_ARRAYLIST_STUB.clear();
        INDEX_ARRAYLIST_STUB.add(INDEX_FIRST_STUDENT);
        VersionedModel expectedModel = new VersionedModelManager(
                new AcademyDirectory(model.getAcademyDirectory()), new UserPrefs());
        Student editedStudent = new StudentBuilder(model.getFilteredStudentList().get(0)).build();

        final int[] partArr = new int[12];
        partArr[0] = 12;

        final boolean[] attArr = new boolean[12];
        attArr[0] = true;

        Participation expectedParticipation = new Participation(12);
        expectedParticipation.setParticipation(partArr);

        Attendance expectedAttendance = new Attendance(12);
        expectedAttendance.setAttendance(attArr);

        editedStudent.setParticipation(expectedParticipation);
        editedStudent.setAttendance(expectedAttendance);

        expectedModel.setStudent(model.getFilteredStudentList().get(0), editedStudent);

        ParticipationCommand participationCommand =
                new ParticipationCommand(12, STUDIO_SESSION_STUB, INDEX_ARRAYLIST_STUB);

        assertCommandSuccess(participationCommand, model,
                ParticipationCommand.MESSAGE_UPDATE_PARTICIPATION_SUCCESS, expectedModel);

        Participation resetParticipation = new Participation(12);
        Attendance resetAttendance = new Attendance(12);

        TypicalStudents.getTypicalAcademyDirectory().getStudentList().get(0).setParticipation(resetParticipation);
        TypicalStudents.getTypicalAcademyDirectory().getStudentList().get(0).setAttendance(resetAttendance);
        INDEX_ARRAYLIST_STUB.clear();
    }


    @Test
    public void equals() {
        INDEX_ARRAYLIST_STUB.add(INDEX_FIRST_STUDENT);
        INDEX_ARRAYLIST_STUB_2.add(INDEX_SECOND_STUDENT);
        final ParticipationCommand standardCommand =
                new ParticipationCommand(12, STUDIO_SESSION_STUB, INDEX_ARRAYLIST_STUB);

        final ParticipationCommand commandWithSameValues =
                new ParticipationCommand(12, STUDIO_SESSION_STUB, INDEX_ARRAYLIST_STUB);

        // same values -> returns true
        assertEquals(standardCommand, commandWithSameValues);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(null, standardCommand);

        // different types -> returns false
        assertNotEquals(standardCommand, new ClearCommand());

        // different indexArrayList
        assertNotEquals(standardCommand,
                new ParticipationCommand(12, STUDIO_SESSION_STUB, INDEX_ARRAYLIST_STUB_2));

        // different participation
        assertNotEquals(standardCommand,
                new ParticipationCommand(11, STUDIO_SESSION_STUB, INDEX_ARRAYLIST_STUB));

        // different session
        assertNotEquals(standardCommand,
                new ParticipationCommand(12, 2, INDEX_ARRAYLIST_STUB));
        INDEX_ARRAYLIST_STUB.clear();
    }
}
