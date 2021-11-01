package seedu.academydirectory.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_ASSESSMENT;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_GRADE;

import java.util.List;
import java.util.Optional;

import seedu.academydirectory.commons.core.Messages;
import seedu.academydirectory.commons.core.index.Index;
import seedu.academydirectory.logic.commands.exceptions.CommandException;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.student.Assessment;
import seedu.academydirectory.model.student.Student;

/**
 * A class that implements the command to record the Student's grade for an assessment.
 */
public class GradeCommand extends Command {

    public static final String COMMAND_WORD = "grade";

    public static final String HELP_MESSAGE = "### Adding grades for an assessment:  `grade`\n"
            + "Avengers will be able to add a student’s grade for a particular assessment.\n"
            + "\n"
            + "Format: `grade INDEX as/ASSESSMENT g/GRADE`\n"
            + "\n"
            + "The input `ASSESSMENT` must be one of the following: RA1, MIDTERM, RA2, PE, FINAL.\n"
            + "The input `GRADE` must be a non-negative integer from 0 to 100 inclusive.\n"
            + "\n"
            + "Example:\n"
            + "* `grade 1 as/RA1 g/15`";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Record the Student's grade for an assessment. "
            + "Parameters: "
            + "INDEX "
            + PREFIX_ASSESSMENT + "ASSESSMENT "
            + PREFIX_GRADE + "GRADE \n"
            + "Example: " + COMMAND_WORD + " "
            + "1 "
            + PREFIX_ASSESSMENT + "RA1 "
            + PREFIX_GRADE + "16";

    public static final String MESSAGE_SUCCESS = "%1$s's %2$s grade updated!";

    private final Index index;
    private final String assessment;
    private final Integer grade;

    /**
     * Constructor for GradeCommand.
     * @param index The student whose grade is to be recorded.
     * @param assessment The assessment.
     * @param grade The grade.
     */
    public GradeCommand(Index index, String assessment, Integer grade) {
        requireAllNonNull(index, assessment, grade);
        this.index = index;
        this.assessment = assessment;
        this.grade = grade;
    }

    @Override
    public CommandResult execute(VersionedModel model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentToEdit = lastShownList.get(index.getZeroBased());
        Assessment assessmentToEdit = studentToEdit.getAssessment().getCopy();
        assessmentToEdit.updateAssessmentGrade(assessment, grade);
        Student editedStudent = new Student(
                studentToEdit.getName(), studentToEdit.getPhone(), studentToEdit.getEmail(),
                studentToEdit.getTelegram(), studentToEdit.getStudioRecord(),
                assessmentToEdit, studentToEdit.getTags());
        model.setStudent(studentToEdit, editedStudent);
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedStudent.getName(), assessment),
                Optional.of(String.format(MESSAGE_SUCCESS, editedStudent.getName(), assessment)));

    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GradeCommand)) {
            return false;
        }

        // state check
        GradeCommand e = (GradeCommand) other;
        return index.equals(e.index)
                && assessment.equals(e.assessment)
                && grade.equals(e.grade);
    }
}
