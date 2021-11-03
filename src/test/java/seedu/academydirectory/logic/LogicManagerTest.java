package seedu.academydirectory.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.academydirectory.commons.core.Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX;
import static seedu.academydirectory.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.academydirectory.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.academydirectory.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.academydirectory.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.academydirectory.logic.commands.CommandTestUtil.TELEGRAM_DESC_AMY;
import static seedu.academydirectory.testutil.Assert.assertThrows;
import static seedu.academydirectory.testutil.TypicalStudents.AMY;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.academydirectory.logic.commands.AddCommand;
import seedu.academydirectory.logic.commands.CommandResult;
import seedu.academydirectory.logic.commands.ListCommand;
import seedu.academydirectory.logic.commands.exceptions.CommandException;
import seedu.academydirectory.logic.parser.exceptions.ParseException;
import seedu.academydirectory.model.ReadOnlyAcademyDirectory;
import seedu.academydirectory.model.UserPrefs;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.VersionedModelManager;
import seedu.academydirectory.model.student.Student;
import seedu.academydirectory.storage.JsonAcademyDirectoryStorage;
import seedu.academydirectory.storage.JsonUserPrefsStorage;
import seedu.academydirectory.storage.StorageManager;
import seedu.academydirectory.testutil.StudentBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");

    @TempDir
    public Path temporaryFolder;

    private final VersionedModel model = new VersionedModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        JsonAcademyDirectoryStorage academyDirectoryStorage =
                new JsonAcademyDirectoryStorage(temporaryFolder.resolve("academyDirectory.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(academyDirectoryStorage, userPrefsStorage,
                temporaryFolder.resolve("vc"));
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        String deleteCommand = "delete 9";
        assertCommandException(deleteCommand, MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        String listCommand = ListCommand.COMMAND_WORD;
        assertCommandSuccess(listCommand, ListCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        // Setup LogicManager with JsonAcademyDirectoryIoExceptionThrowingStub
        JsonAcademyDirectoryStorage academyDirectoryStorage =
                new JsonAcademyDirectoryIoExceptionThrowingStub(
                        temporaryFolder.resolve("ioExceptionAcademyDirectory.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ioExceptionUserPrefs.json"));
        StorageManager storage = new StorageManager(academyDirectoryStorage, userPrefsStorage,
                temporaryFolder.resolve("vc"));
        logic = new LogicManager(model, storage);

        // Execute add command
        String addCommand = AddCommand.COMMAND_WORD + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + TELEGRAM_DESC_AMY;
        Student expectedStudent = new StudentBuilder(AMY).withTags().build();
        VersionedModelManager expectedModel = new VersionedModelManager();
        expectedModel.addStudent(expectedStudent);
        String expectedMessage = LogicManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION;
        assertCommandFailure(addCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void getFilteredStudentList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredStudentList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, VersionedModel)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
                                      VersionedModel expectedModel) throws CommandException, ParseException {
        CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, VersionedModel)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, VersionedModel)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, VersionedModel)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        VersionedModel expectedModel = new VersionedModelManager(model.getAcademyDirectory(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, VersionedModel expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonAcademyDirectoryIoExceptionThrowingStub extends JsonAcademyDirectoryStorage {
        private JsonAcademyDirectoryIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveAcademyDirectory(ReadOnlyAcademyDirectory academyDirectory, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }
}
