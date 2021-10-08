package seedu.academydirectory.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_ASSESSMENT;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_GRADE;

import java.util.List;

import seedu.academydirectory.commons.core.Messages;
import seedu.academydirectory.commons.core.index.Index;
import seedu.academydirectory.logic.commands.exceptions.CommandException;
import seedu.academydirectory.model.Model;
import seedu.academydirectory.model.student.Assessment;
import seedu.academydirectory.model.student.Student;

/**
 * A class that implements the command to record the Student's grade for an assessment.
 */
public class GradeCommand extends Command {

    public static final String COMMAND_WORD = "grade";

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
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentToEdit = lastShownList.get(index.getZeroBased());
        Assessment assessmentToEdit = studentToEdit.getAssessment();
        assessmentToEdit.updateAssessmentGrade(assessment, grade);
        Student editedStudent = new Student(
                studentToEdit.getName(), studentToEdit.getPhone(), studentToEdit.getEmail(),
                studentToEdit.getAddress(), studentToEdit.getStudioRecord(), studentToEdit.getAssessment(),
                studentToEdit.getTags());
        model.setStudent(studentToEdit, editedStudent);
        model.updateFilteredStudentList(Model.PREDICATE_SHOW_ALL_STUDENTS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, editedStudent.getName(), assessment));
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
