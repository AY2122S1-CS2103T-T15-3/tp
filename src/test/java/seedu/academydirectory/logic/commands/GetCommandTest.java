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

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.academydirectory.logic.parser.Prefix;
import seedu.academydirectory.model.UserPrefs;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.VersionedModelManager;
import seedu.academydirectory.model.student.Name;
import seedu.academydirectory.model.student.NameContainsKeywordsPredicate;
import seedu.academydirectory.model.student.PersonalDetail;

/**
 * Contains integration tests (interaction with the VersionedModel) for {@code FindCommand}.
 */
public class GetCommandTest {
    @TempDir
    public static Path tempPath;

    private static final Function<Path, VersionedModel> modelSupplier = (Path path) -> new VersionedModelManager(
            getTypicalAcademyDirectory(),
            getTempUserPref(path));
    private final Function<Path, VersionedModel> expectedModelSupplier = (Path path) -> new VersionedModelManager(
            getTypicalAcademyDirectory(),
            getTempUserPref(path));;

    @Test
    public void equals() {
        GetCommand emailGetCommand = new GetCommand(List.of(PREFIX_EMAIL), List.of());
        GetCommand telegramGetCommand = new GetCommand(List.of(PREFIX_TELEGRAM), List.of());

        // same object -> returns true
        assertEquals(emailGetCommand, emailGetCommand);
        assertEquals(telegramGetCommand, telegramGetCommand);

        // same values -> returns true
        GetCommand emailGetCommandCopy = new GetCommand(List.of(PREFIX_EMAIL), List.of());
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
        VersionedModel model = modelSupplier.apply(tempPath);
        VersionedModel expectedModel = modelSupplier.apply(tempPath);
        GetCommand.SUPPORTED_PREFIX.forEach(prefix -> execute_singlePrefix(prefix, model, null, expectedModel));
    }

    @Test
    public void execute_singlePrefixWithNameNonEmptyModel() {
        VersionedModel model = modelSupplier.apply(tempPath);
        VersionedModel expectedModel = expectedModelSupplier.apply(tempPath);
        getTypicalStudents()
                .forEach(student -> GetCommand.SUPPORTED_PREFIX
                        .forEach(prefix -> execute_singlePrefix(prefix, model, student.getName(), expectedModel)));
        ;
    }

    private void execute_singlePrefix(Prefix prefix, VersionedModel model, Name name, VersionedModel expectedModel) {
        List<String> nameList = name == null
                ? List.of()
                : List.of(name.fullName.split("\\s"));

        NameContainsKeywordsPredicate predicate = name == null
                ? null
                : new NameContainsKeywordsPredicate(nameList);

        ObservableList<PersonalDetail> expectedResponse = model.getAcademyDirectory()
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
        GetCommand command = new GetCommand(List.of(prefix), nameList);
        command.execute(model);
        assertEquals(model.getAdditionalViewModel().getAdditionalInfo().get(), content);
        assertCommandSuccess(command, model, GetCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_singlePrefixEmptyModel() {
        VersionedModel emptyModel = new VersionedModelManager();

        GetCommand.SUPPORTED_PREFIX.forEach(prefix -> {
            GetCommand command = new GetCommand(List.of(prefix), List.of());
            command.execute(emptyModel);
            assertEquals(emptyModel.getAdditionalViewModel().getAdditionalInfo().get(), MESSAGE_NOTHING_TO_SHOW);
            assertCommandSuccess(command, emptyModel, GetCommand.MESSAGE_FAILED, emptyModel);
        });
    }

    private static UserPrefs getTempUserPref(Path path) {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAcademyDirectoryFilePath(path);
        userPrefs.setVersionControlPath(path);
        return userPrefs;
    }
}
