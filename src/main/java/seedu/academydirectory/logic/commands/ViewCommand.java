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
     * @param model {@code Model} which the command should operate on.
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
