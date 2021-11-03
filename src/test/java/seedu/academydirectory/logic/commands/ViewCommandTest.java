package seedu.academydirectory.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.academydirectory.logic.commands.CommandTestUtil.showStudentAtIndex;
import static seedu.academydirectory.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static seedu.academydirectory.testutil.TypicalIndexes.INDEX_SECOND_STUDENT;
import static seedu.academydirectory.testutil.TypicalIndexes.INDEX_THIRD_STUDENT;
import static seedu.academydirectory.testutil.TypicalStudents.getTypicalAcademyDirectory;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.commons.core.Messages;
import seedu.academydirectory.model.UserPrefs;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.VersionedModelManager;

public class ViewCommandTest {
    private final VersionedModel model = new VersionedModelManager(getTypicalAcademyDirectory(), new UserPrefs());

    @Test
    public void valid_view_command() {
        ViewCommand viewCommand1 = new ViewCommand(INDEX_FIRST_STUDENT);
        ViewCommand viewCommand2 = new ViewCommand(INDEX_SECOND_STUDENT);
        ViewCommand viewCommand3 = new ViewCommand(INDEX_THIRD_STUDENT);
        assertEquals(viewCommand1, new ViewCommand(INDEX_FIRST_STUDENT));
        assertEquals(viewCommand2, new ViewCommand(INDEX_SECOND_STUDENT));
        assertNotEquals(viewCommand3, new ViewCommand(INDEX_FIRST_STUDENT));
        assertNotEquals(viewCommand1, viewCommand2);
        assertNotEquals(viewCommand2, "Life is good");
    }

    @Test
    public void invalid_execution() {
        showStudentAtIndex(model, INDEX_FIRST_STUDENT);
        assertTrue(INDEX_SECOND_STUDENT.getZeroBased() < model.getAcademyDirectory().getStudentList().size());
        ViewCommand viewCommand3 = new ViewCommand(INDEX_SECOND_STUDENT);
        assertCommandFailure(viewCommand3, model, Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }
}
