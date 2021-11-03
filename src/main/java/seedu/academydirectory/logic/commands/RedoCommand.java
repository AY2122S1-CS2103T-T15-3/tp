package seedu.academydirectory.logic.commands;

import static seedu.academydirectory.model.VersionControl.CURRENT_LABEL_STRING;

import seedu.academydirectory.logic.commands.exceptions.CommandException;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.versioncontrol.objects.Commit;

public class RedoCommand extends Command {
    public static final String COMMAND_WORD = "redo";

    public static final String HELP_MESSAGE = "### Redo changes : `redo`\n"
            + "\n"
            + "Redo changes done to Academy Directory. Use the history command to preview what redo will do\n"
            + "\n"
            + "Format: `redo`";

    public static final String MESSAGE_SUCCESS = "Successfully redo changes to Academy"
            + " Directory as requested!";

    public static final String REDO_REQUEST_REJECTED = "Unable to redo Academy Directory as requested ...";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Redo changes to Academy Directory "
            + "Example: " + COMMAND_WORD;

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code VersionedModel} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    @Override
    public CommandResult execute(VersionedModel model) throws CommandException {
        Commit currLatest = model.fetchCommitByLabel(CURRENT_LABEL_STRING);
        Commit nextCommit = currLatest.getHighestAncestor(model.getHeadCommit());
        if (nextCommit.equals(Commit.emptyCommit())) {
            throw new CommandException(REDO_REQUEST_REJECTED + " Is there anything to redo?");
        }

        new RevertCommand(nextCommit.getHash()).execute(model);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
