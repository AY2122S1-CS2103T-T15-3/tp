package seedu.academydirectory.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.academydirectory.logic.commands.GetCommand.MESSAGE_NOTHING_TO_SHOW;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.academydirectory.testutil.TypicalStudents.getTypicalAcademyDirectory;
import static seedu.academydirectory.testutil.TypicalStudents.getTypicalStudents;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.academydirectory.logic.parser.Prefix;
import seedu.academydirectory.model.ModelManager;
import seedu.academydirectory.model.UserPrefs;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.student.Information;
import seedu.academydirectory.model.student.InformationWantedFunction;
import seedu.academydirectory.model.student.Name;
import seedu.academydirectory.model.student.NameContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class GetCommandTest {
    private final VersionedModel model = new ModelManager(getTypicalAcademyDirectory(), new UserPrefs());
    private final VersionedModel expectedModel = new ModelManager(getTypicalAcademyDirectory(), new UserPrefs());

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

    private void execute_singlePrefix(Prefix prefix, VersionedModel model, Name name) {
        NameContainsKeywordsPredicate predicate = name == null
                ? null
                : new NameContainsKeywordsPredicate(List.of(name.fullName.split("\\s")));

        InformationWantedFunction function = new InformationWantedFunction(prefix, predicate);

        ObservableList<Information> expectedResponse = model.getAcademyDirectory()
                .getStudentList().stream()
                .filter(x -> predicate == null || predicate.test(x))
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
        String content = expectedResponse.stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        GetCommand command = new GetCommand(function);
        command.execute(model);
        assertEquals(model.getAdditionalViewModel().getAdditionalInfo().get(), content);
        assertCommandSuccess(command, model, GetCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_singlePrefixEmptyModel() {
        VersionedModel emptyModel = new ModelManager();

        InformationWantedFunction.SUPPORTED_PREFIX.forEach(prefix -> {
            InformationWantedFunction function = new InformationWantedFunction(prefix);

            GetCommand command = new GetCommand(function);
            command.execute(emptyModel);
            assertEquals(emptyModel.getAdditionalViewModel().getAdditionalInfo().get(), MESSAGE_NOTHING_TO_SHOW);
            assertCommandSuccess(command, emptyModel, GetCommand.MESSAGE_FAILED, emptyModel);
        });
    }
}
