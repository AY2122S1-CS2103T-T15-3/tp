package seedu.academydirectory.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Optional;

import seedu.academydirectory.commons.exceptions.DataConversionException;
import seedu.academydirectory.logic.commands.exceptions.CommandException;
import seedu.academydirectory.model.AcademyDirectory;
import seedu.academydirectory.model.ReadOnlyAcademyDirectory;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.storage.AcademyDirectoryStorage;
import seedu.academydirectory.storage.JsonAcademyDirectoryStorage;
import seedu.academydirectory.storage.StorageManager;
import seedu.academydirectory.versioncontrol.objects.Commit;

public class RevertCommand extends Command {
    public static final String COMMAND_WORD = "revert";

    public static final String HELP_MESSAGE = "### Revert to a history : `revert`\n"
            + "\n"
            + "Reverts storage to an old history in the Academy Directory.\n"
            + "\n"
            + "Format: `revert KEYWORD`";

    public static final String MESSAGE_SUCCESS = "Successfully reverted Academy"
            + " Directory as requested!";

    public static final String REVERT_REQUEST_REJECTED = "Unable to revert Academy Directory as requested ...";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reverts Academy Directory to the stage given by "
            + "the five character hash\n"
            + "Parameters: KEYWORD ...\n"
            + "Example: " + COMMAND_WORD + " 6dfdx";

    private final String fiveDigitHash;
    public RevertCommand(String hash) {
        this.fiveDigitHash = hash;
    }

    @Override
    public CommandResult execute(VersionedModel model) throws CommandException {
        requireNonNull(model);
        try {
            Commit relevantCommit = model.revert(this.fiveDigitHash);
            if (relevantCommit.isEmpty()) {
                throw new CommandException(REVERT_REQUEST_REJECTED + "Hash value correct (and not current state) ?");
            }
        } catch (NullPointerException | IOException e) {
            throw new CommandException(REVERT_REQUEST_REJECTED, e);
        }

        AcademyDirectoryStorage academyDirectoryStorage =
                new JsonAcademyDirectoryStorage(model.getAcademyDirectoryFilePath());
        StorageManager storage = new StorageManager(academyDirectoryStorage,
                null,
                model.getUserPrefs().getVersionControlPath());
        Optional<ReadOnlyAcademyDirectory> academyDirectoryOptional;
        ReadOnlyAcademyDirectory initialData;

        try {
            academyDirectoryOptional = storage.readAcademyDirectory();
            initialData = academyDirectoryOptional.orElseGet(AcademyDirectory::new);
        } catch (DataConversionException | IOException e) {
            initialData = new AcademyDirectory();
        }

        model.setAcademyDirectory(initialData);
        return new CommandResult(MESSAGE_SUCCESS, false, false);
    }
}
