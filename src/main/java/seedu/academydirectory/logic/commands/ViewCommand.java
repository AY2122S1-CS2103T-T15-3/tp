package seedu.academydirectory.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.academydirectory.commons.core.Messages;
import seedu.academydirectory.commons.core.index.Index;
import seedu.academydirectory.logic.AdditionalViewType;
import seedu.academydirectory.logic.commands.exceptions.CommandException;
import seedu.academydirectory.model.AdditionalInfo;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.student.Student;

public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String HELP_MESSAGE = "#### Viewing all related information of a student: `view`\n"
            + "Displays all information relating to a student, both personal and academic information. "
            + "Information shown include the student's name, tag, participation data, examination score, "
            + "and contact information.\n\n"
            + "Format: `view INDEX`\n\n"
            + "* View students' information, both personal and academic related, "
            + "based on their `INDEX` number on the list.\n"
            + "* Information shown for the student will be in a drop-down menu listing participation and grades.\n"
            + "* Contact information like phone number, email address, and Telegram handle are also displayed.\n\n"
            + "Examples:\n"
            + "* `view 1`\n\n"
            + "**Include an interactive GUI version where Avengers can click"
            + " on the list to view student information as well.**";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": View full information about the student.\n"
            + "Parameters: INDEX\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "View information related to %1$s";

    private final Index index;
    /**
     * Constructor for the View Command
     * @param index index student need to retrieve from
     */
    public ViewCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    /**
     * Execute the command View
     * @param model {@code VersionedModel} which the command should operate on.
     * @return Command result that contains the student
     * @throws CommandException when index is not valid
     */
    @Override
    public CommandResult execute(VersionedModel model) throws CommandException {
        requireNonNull(model);
        List<Student> studentList = model.getFilteredStudentList();
        if (index.getZeroBased() >= studentList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }
        Student studentToView = studentList.get(index.getZeroBased());
        model.setAdditionalViewType(AdditionalViewType.VIEW);
        model.setAdditionalInfo(AdditionalInfo.of(studentToView));
        return new CommandResult(String.format(MESSAGE_SUCCESS, studentToView.getName()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ViewCommand)) {
            return false;
        }
        ViewCommand otherViewCommand = (ViewCommand) other;
        return this.index.equals(otherViewCommand.index);
    }
}
