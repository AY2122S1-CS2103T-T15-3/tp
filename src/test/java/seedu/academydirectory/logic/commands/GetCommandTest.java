package seedu.academydirectory.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.academydirectory.testutil.TypicalStudents.getTypicalAcademyDirectory;
import static seedu.academydirectory.testutil.TypicalStudents.getTypicalStudents;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.academydirectory.commons.core.Messages;
import seedu.academydirectory.logic.parser.Prefix;
import seedu.academydirectory.model.Model;
import seedu.academydirectory.model.ModelManager;
import seedu.academydirectory.model.UserPrefs;
import seedu.academydirectory.model.student.Information;
import seedu.academydirectory.model.student.InformationWantedFunction;
import seedu.academydirectory.model.student.Name;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class GetCommandTest {
    private final Model model = new ModelManager(getTypicalAcademyDirectory(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAcademyDirectory(), new UserPrefs());

    @Test
    public void equals() {
        InformationWantedFunction emailRetrieveFunction = new InformationWantedFunction(PREFIX_EMAIL);
        InformationWantedFunction telegramRetrieveFunction = new InformationWantedFunction(PREFIX_TELEGRAM);

        GetCommand emailGetCommand = new GetCommand(emailRetrieveFunction);
        GetCommand telegramGetCommand = new GetCommand(telegramRetrieveFunction);

        // same object -> returns true
        assertEquals(emailGetCommand, emailGetCommand);
        assertEquals(telegramGetCommand, telegramGetCommand);

        // same values -> returns true
        GetCommand emailGetCommandCopy = new GetCommand(emailRetrieveFunction);
        assertEquals(emailGetCommand, emailGetCommandCopy);

        // different types -> returns false
        assertNotEquals(1, emailGetCommand);
        assertNotEquals(0, telegramGetCommand);

        // null -> returns false
        assertFalse(emailGetCommand.equals(null));
        assertFalse(telegramGetCommand.equals(null));

        // different student -> returns false
        assertNotEquals(emailGetCommand, telegramGetCommand);
    }

    @Test
    public void execute_singlePrefixNoNameNonEmptyModel() {
        InformationWantedFunction.SUPPORTED_PREFIX.forEach(prefix -> execute_singlePrefix(prefix, model, null));
    }

    @Test
    public void execute_singlePrefixWithNameNonEmptyModel() {
        getTypicalStudents()
                .forEach(student -> InformationWantedFunction.SUPPORTED_PREFIX
                        .forEach(prefix -> execute_singlePrefix(prefix, model, student.getName())));
        ;
    }

    private void execute_singlePrefix(Prefix prefix, Model model, Name name) {
        InformationWantedFunction function = new InformationWantedFunction(prefix, name);

        ObservableList<Information> expectedResponse = model.getAcademyDirectory()
                .getStudentList().stream()
                .filter(x -> name == null || x.getName().equals(name))
                .map(x -> {
                    if (PREFIX_EMAIL.equals(prefix)) {
                        return x.getEmail();
                    } else if (PREFIX_TELEGRAM.equals(prefix)) {
                        return x.getTelegram();
                    } else if (PREFIX_PHONE.equals(prefix)) {
                        return x.getPhone();
                    }
                    return null;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        String expectedMessage = expectedResponse.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));

        GetCommand command = new GetCommand(function);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_singlePrefixEmptyModel() {
        Model emptyModel = new ModelManager();
        String expectedMessage = String.format(Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW, 0);

        InformationWantedFunction.SUPPORTED_PREFIX.forEach(prefix -> {
            InformationWantedFunction function = new InformationWantedFunction(prefix);

            GetCommand command = new GetCommand(function);
            assertCommandSuccess(command, emptyModel, expectedMessage, emptyModel);
        });
    }
}
