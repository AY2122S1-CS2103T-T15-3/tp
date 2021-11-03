package seedu.academydirectory.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.academydirectory.testutil.Assert.assertThrows;
import static seedu.academydirectory.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.academydirectory.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.academydirectory.testutil.TypicalStudents.getTypicalAcademyDirectory;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.commons.core.Messages;
import seedu.academydirectory.commons.core.index.Index;
import seedu.academydirectory.model.AcademyDirectory;
import seedu.academydirectory.model.UserPrefs;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.VersionedModelManager;
import seedu.academydirectory.model.student.Assessment;
import seedu.academydirectory.model.student.Student;
import seedu.academydirectory.testutil.StudentBuilder;
import seedu.academydirectory.testutil.TypicalStudents;

class GradeCommandTest {

    private VersionedModel model = new VersionedModelManager(getTypicalAcademyDirectory(), new UserPrefs());
    private final HashMap<String, Integer> assessmentHashMap = new Assessment().getAssessmentHashMap();
    private final String validAssessmentName1 = "RA1";
    private final String validAssessmentName2 = "MIDTERM";
    private final Integer validGrade1 = 10;
    private final Integer validGrade2 = 20;

    @Test
    public void constructor_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new GradeCommand(null, null, null));
    }

    @Test
    void execute_addGrade_success() {
        Student firstStudent = model.getFilteredStudentList().get(0);
        assessmentHashMap.replace(validAssessmentName1, validGrade1);
        Student editedStudent = new StudentBuilder(firstStudent).withAssessment(assessmentHashMap).build();
        GradeCommand addGradeCommand =
                new GradeCommand(INDEX_FIRST_STUDENT, validAssessmentName1, validGrade1);
        String expectedMessage =
                String.format(GradeCommand.MESSAGE_SUCCESS, editedStudent.getName(), validAssessmentName1);
        VersionedModel expectedModel = new VersionedModelManager(
                new AcademyDirectory(model.getAcademyDirectory()), new UserPrefs());
        expectedModel.setStudent(firstStudent, editedStudent);
        assertCommandSuccess(addGradeCommand, model, expectedMessage, expectedModel);

        // reset model
        TypicalStudents.getTypicalAcademyDirectory().getStudentList().get(0).setAssessment(new Assessment());
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        GradeCommand gradeCommand = new GradeCommand(outOfBoundIndex, validAssessmentName1, validGrade1);

        assertCommandFailure(gradeCommand, model, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final GradeCommand standardCommand =
                new GradeCommand(INDEX_FIRST_STUDENT, validAssessmentName1, validGrade1);

        // same values -> returns true
        GradeCommand commandWithSameValues =
                new GradeCommand(INDEX_FIRST_STUDENT, validAssessmentName1, validGrade1);
        assertEquals(standardCommand, commandWithSameValues);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(null, standardCommand);

        // different types -> returns false
        assertNotEquals(standardCommand, new ClearCommand());

        // different index -> returns false
        assertNotEquals(standardCommand, new GradeCommand(INDEX_SECOND_STUDENT, validAssessmentName1, validGrade1));

        // different assessment -> returns false
        assertNotEquals(standardCommand, new GradeCommand(INDEX_FIRST_STUDENT, validAssessmentName2, validGrade1));

        // different grade -> returns false
        assertNotEquals(standardCommand, new GradeCommand(INDEX_FIRST_STUDENT, validAssessmentName1, validGrade2));
    }

}
