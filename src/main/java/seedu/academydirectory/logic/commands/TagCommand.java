package seedu.academydirectory.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.academydirectory.commons.core.Messages;
import seedu.academydirectory.commons.core.index.Index;
import seedu.academydirectory.logic.commands.exceptions.CommandException;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.student.Student;
import seedu.academydirectory.model.tag.Tag;

/**
 * Represents a command that tags a student.
 */
public class TagCommand extends Command {
    public static final String COMMAND_WORD = "tag";

    public static final String HELP_MESSAGE = "### Assigns tag(s) to a student: `tag`\\\n"
            + "Tutors will be able to tag a student with relevant information.\\\n"
            + "Format: `tag INDEX t/tag [t/tag] . . . `\\\n"
            + "Recommended use of tags:\n"
            + "- Set a reminder to mark the student's mission with a `mission` tag\n"
            + "- Note down the topics that the student needs help with\\\n"
            + "Entering the tag command with a single empty tag `t/` will remove all tags assigned to the student.\n"
            + "Tags names should be alphanumeric and consist of only one word.\n"
            + "For multiple tag entries, none of the tags should be empty.\n"
            + "All existing tags will be replaced by the new tags.\n"
            + "Example:\n"
            + "tag 1 t/\n"
            + "tag 1 t/mission\n"
            + "tag 1 t/streams t/envModel";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns tag(s) to a student.\n"
            + "Parameters: "
            + "INDEX "
            + PREFIX_TAG + "TAG...\n"
            + "Example: " + COMMAND_WORD + " "
            + "1 "
            + PREFIX_TAG + "mission "
            + PREFIX_TAG + "streams";

    public static final String MESSAGE_SUCCESS = "%1$s's tag(s) updated!";

    private final Index index;
    private final Set<Tag> tag;

    /**
     * Creates a TagCommand to add the specified {@code Tag}
     * @param tag The tag to be assigned.
     */
    public TagCommand(Index index, Set<Tag> tag) {
        requireAllNonNull(index, tag);
        this.index = index;
        this.tag = tag;
    }

    @Override
    public CommandResult execute(VersionedModel model) throws CommandException {
        requireNonNull(model);

        List<Student> lastShownList = model.getFilteredStudentList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentToEdit = lastShownList.get(index.getZeroBased());
        Set<Tag> editedTagSet = new HashSet<>(tag);
        Student editedStudent = new Student(
                studentToEdit.getName(), studentToEdit.getPhone(), studentToEdit.getEmail(),
                studentToEdit.getTelegram(), studentToEdit.getStudioRecord(),
                studentToEdit.getAssessment(), editedTagSet);
        model.setStudent(studentToEdit, editedStudent);
        model.updateFilteredStudentList(VersionedModel.PREDICATE_SHOW_ALL_STUDENTS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, studentToEdit.getName()));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagCommand)) {
            return false;
        }

        // state check
        TagCommand e = (TagCommand) other;
        return index.equals(e.index)
                && tag.equals(e.tag);
    }
}
