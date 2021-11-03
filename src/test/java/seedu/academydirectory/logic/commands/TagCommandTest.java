package seedu.academydirectory.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.academydirectory.testutil.Assert.assertThrows;
import static seedu.academydirectory.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.academydirectory.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.academydirectory.testutil.TypicalStudents.getTypicalAcademyDirectory;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.commons.core.Messages;
import seedu.academydirectory.commons.core.index.Index;
import seedu.academydirectory.model.AcademyDirectory;
import seedu.academydirectory.model.UserPrefs;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.VersionedModelManager;
import seedu.academydirectory.model.student.Student;
import seedu.academydirectory.model.tag.Tag;
import seedu.academydirectory.testutil.StudentBuilder;

class TagCommandTest {

    private final VersionedModel model = new VersionedModelManager(getTypicalAcademyDirectory(), new UserPrefs());
    private final Set<Tag> tagSet = new HashSet<>();
    private final String validTagName1 = "mission";
    private final String validTagName2 = "streams2";

    @Test
    public void constructor_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TagCommand(null, null));
    }

    @Test
    void execute_addTag_success() {
        Student firstStudent = model.getFilteredStudentList().get(0);

        Student editedStudent = new StudentBuilder(firstStudent).withTags().build();
        TagCommand addTagCommand =
                new TagCommand(INDEX_FIRST_STUDENT, tagSet);
        String expectedMessage =
                String.format(TagCommand.MESSAGE_SUCCESS, editedStudent.getName());
        VersionedModel expectedModel = new VersionedModelManager(
                new AcademyDirectory(model.getAcademyDirectory()), new UserPrefs());
        expectedModel.setStudent(firstStudent, editedStudent);
        assertCommandSuccess(addTagCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        TagCommand tagCommand = new TagCommand(outOfBoundIndex, tagSet);

        assertCommandFailure(tagCommand, model, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        Set<Tag> tagSet1 = new HashSet<>();
        Set<Tag> tagSet2 = new HashSet<>();
        tagSet1.add(new Tag(validTagName1));
        tagSet2.add(new Tag(validTagName2));

        final TagCommand standardCommand =
                new TagCommand(INDEX_FIRST_STUDENT, tagSet1);

        // same values -> returns true
        TagCommand commandWithSameValues =
                new TagCommand(INDEX_FIRST_STUDENT, tagSet1);
        assertEquals(standardCommand, commandWithSameValues);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(null, standardCommand);

        // different types -> returns false
        assertNotEquals(standardCommand, new ClearCommand());

        // different index -> returns false
        assertNotEquals(standardCommand, new TagCommand(INDEX_SECOND_STUDENT, tagSet1));

        // different grade -> returns false
        assertNotEquals(standardCommand, new TagCommand(INDEX_FIRST_STUDENT, tagSet2));
    }
}
