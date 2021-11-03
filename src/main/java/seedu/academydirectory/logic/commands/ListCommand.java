package seedu.academydirectory.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.model.VersionedModel.PREDICATE_SHOW_ALL_STUDENTS;

import seedu.academydirectory.model.VersionedModel;

/**
 * Lists all students in the academy directory to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String HELP_MESSAGE = "### Listing all students : `list`\n"
            + "\n"
            + "Shows a list of all students in the academy directory.\n"
            + "\n"
            + "Format: `list`";
    public static final String MESSAGE_SUCCESS = "Listed all students";


    @Override
    public CommandResult execute(VersionedModel model) {
        requireNonNull(model);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
