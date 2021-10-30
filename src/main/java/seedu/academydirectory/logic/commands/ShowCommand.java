package seedu.academydirectory.logic.commands;

import static java.util.Objects.requireNonNull;

import javafx.collections.ObservableList;
import seedu.academydirectory.logic.AdditionalViewType;
import seedu.academydirectory.model.AdditionalInfo;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.student.Student;

/**
 * A class that implements the command to show all the students' grades for an assessment.
 */
public class ShowCommand extends Command {

    public static final String COMMAND_WORD = "show";

    public static final String HELP_MESSAGE = "### Show grades for an assessment:  `show`\n"
            + "Avengers will be able to view all students' grades for a particular assessment.\n"
            + "\n"
            + "Format: `show ASSESSMENT`\n"
            + "\n"
            + "Record students' `GRADE` for the `ASSESSMENT`.\n"
            + "The input `ASSESSMENT` must be a valid assessment: RA1, MIDTERM, RA2, PE, FINAL.\n"
            + "\n"
            + "Example:\n"
            + "* `show RA1`";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Show students' grades for an assessment. "
            + "\nParameters: "
            + "ASSESSMENT "
            + "Example: " + COMMAND_WORD + " "
            + "RA1";

    public static final String MESSAGE_SUCCESS = "Grades for %1$s displayed.";

    private final String assessment;

    /**
     * Constructor for ShowCommand.
     * @param assessment The assessment's grades that will be displayed.
     */
    public ShowCommand(String assessment) {
        requireNonNull(assessment);
        this.assessment = assessment.toUpperCase();
    }

    /**
     * Return a String representation of the collated grades for a particular Assessment.
     * @param students The students in the AcademyDirectory.
     * @param assessment The specified Assessment.
     * @return A String representation of the grades for a particular Assessment.
     */
    public static String displayResult(ObservableList<Student> students, String assessment) {
        StringBuilder result = new StringBuilder();
        result.append("Results for ").append(assessment).append("\n");
        double totalMarks = 0;
        for (Student student : students) {
            String name = student.getName().fullName;
            String grade = student.getAssessment().getAssessmentGrade(assessment);
            totalMarks += grade.equals("NA") ? 0 : Integer.parseInt(grade);
            result.append(name).append(": ").append(grade).append("\n");
        }
        String average = String.format("%.2f", totalMarks / (students.toArray().length));
        result.append("\n").append("Average: ").append(average);
        return result.toString();
    }

    @Override
    public CommandResult execute(VersionedModel model) {
        requireNonNull(model);
        ObservableList<Student> students = model.getFilteredStudentList();
        String result = displayResult(students, this.assessment);
        model.setAdditionalViewType(AdditionalViewType.TEXT);
        model.setAdditionalInfo(AdditionalInfo.of(result));
        return new CommandResult(String.format(MESSAGE_SUCCESS, assessment));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ShowCommand)) {
            return false;
        }

        ShowCommand e = (ShowCommand) other;
        return assessment.equals(e.assessment);
    }
}
