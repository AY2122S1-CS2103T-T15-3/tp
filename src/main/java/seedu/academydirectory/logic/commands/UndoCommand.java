package seedu.academydirectory.logic.commands;

import seedu.academydirectory.logic.commands.exceptions.CommandException;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.versioncontrol.objects.Commit;

public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";

    public static final String HELP_MESSAGE = "### Undo changes : `undo`\n"
            + "\n"
            + "Undo changes done to Academy Directory. Use the history command to preview what undo will do\n"
            + "\n"
            + "Format: `undo`";

    public static final String MESSAGE_SUCCESS = "Successfully undone changes to Academy"
            + " Directory as requested!";

    public static final String UNDO_REQUEST_REJECTED = "Unable to undo Academy Directory as requested ...";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Undo changes to Academy Directory "
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
        Commit prevCommit = model.getHeadCommit().getParentSupplier().get();
        if (prevCommit.isEmpty()) {
            throw new CommandException(UNDO_REQUEST_REJECTED + " Is there anything to undo?");
        }

        new RevertCommand(prevCommit.getHash()).execute(model);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
