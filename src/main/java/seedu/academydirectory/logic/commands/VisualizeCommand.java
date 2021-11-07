package seedu.academydirectory.logic.commands;

import static seedu.academydirectory.model.VersionedModel.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import seedu.academydirectory.logic.AdditionalViewType;
import seedu.academydirectory.logic.commands.exceptions.CommandException;
import seedu.academydirectory.model.AdditionalInfo;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.student.Assessment;
import seedu.academydirectory.model.student.Student;

public class VisualizeCommand extends Command {

    public static final String COMMAND_WORD = "visualize";

    public static final String HELP_MESSAGE = "### Visualize class assessment statistic : `visualize`\n"
            + "\n"
            + "Tutors will be able to visualize class performance in assessment with box and whisker plot.\n"
            + "\n"
            + "Format: `visualize`\n"
            + "\n"
            + "Examples:\n"
            + "* `visualize`";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + "visualize class performance in assessment with box and whisker plot. ";

    public static final String MESSAGE_VISUALIZE_SUCCESS = "Class Performance in Assessment shown";

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code VersionedModel} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    @Override
    public CommandResult execute(VersionedModel model) throws CommandException {
        //Always go back to the full list of students
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        List<Student> studentList = model.getFilteredStudentList();

        Map<String, List<Integer>> classAssessmentResults = studentList
                                .stream()
                                .map(Student::getAssessment)
                                .map(Assessment::getAssessmentHashMap)
                                .flatMap(hashMap -> hashMap.entrySet().stream())
                                .collect(Collectors.groupingBy(
                                        Map.Entry::getKey,
                                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        LinkedHashMap<String, List<Integer>> orderedAssessmentResults = new LinkedHashMap<>();

        for (String assessment: Assessment.ASSESSMENT_LIST) {
            orderedAssessmentResults.put(assessment, classAssessmentResults.get(assessment));
        }

        model.setAdditionalViewType(AdditionalViewType.VISUALIZE);
        model.setAdditionalInfo(AdditionalInfo.of(orderedAssessmentResults));

        return new CommandResult(MESSAGE_VISUALIZE_SUCCESS);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof VisualizeCommand);
    }
}
