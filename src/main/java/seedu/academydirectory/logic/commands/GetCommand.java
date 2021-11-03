package seedu.academydirectory.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.academydirectory.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.academydirectory.logic.AdditionalViewType;
import seedu.academydirectory.model.AdditionalInfo;
import seedu.academydirectory.model.Model;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.student.PersonalDetail;
import seedu.academydirectory.model.student.PersonalDetailRetriever;

/**
 * Get personal detail of a student or students.
 */
public class GetCommand extends Command {
    public static final String COMMAND_WORD = "get";

    public static final String HELP_MESSAGE = "### Get personal detail of students': `get`\n"
            + "\n"
            + "Gets personal details of all students if no name is specified, or gets "
            + "personal details of all students whose name contain the specified name."
            + " Currently supported personal details includes: \n"
            + "- Email\n"
            + "- Telegram Handle\n"
            + "- Phone Number\n"
            + "\n"
            + "Format: `get INFORMATION [MORE INFORMATION] [STUDENT_NAME]`";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Get personal details of students "
            + "and displays them as a list\n"
            + "Parameters: " + PREFIX_EMAIL + " | "
            + PREFIX_TELEGRAM + " | " + PREFIX_PHONE + "\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_PHONE + " " + PREFIX_NAME + "Alex";
    public static final String MESSAGE_SUCCESS = "Personal details retrieval is successful";
    public static final String MESSAGE_FAILED = "Failed to receive one or more personal details. Showing what I can...";
    public static final String MESSAGE_NOTHING_TO_SHOW = "Nothing to show...";

    private final List<PersonalDetailRetriever> functionList;

    public GetCommand(List<PersonalDetailRetriever> functionList) {
        this.functionList = functionList;
    }

    public GetCommand(PersonalDetailRetriever filter) {
        this(List.of(filter));
    }

    private String executeFilter(Model model, PersonalDetailRetriever personalDetailRetriever) {
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        ObservableList<PersonalDetail> view = model.getFilteredStudentList()
                .stream().map(personalDetailRetriever)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        return view.size() == 0
                ? null
                : view.stream().map(Object::toString).collect(Collectors.joining("\n"));
    }

    @Override
    public CommandResult execute(VersionedModel model) {
        requireNonNull(model);
        List<String> result = functionList.stream().parallel().map(x -> executeFilter(model, x))
                .collect(Collectors.toList());
        String feedbackMessage = result.contains(null) ? MESSAGE_FAILED : MESSAGE_SUCCESS;
        String resultString = result.stream().allMatch(Objects::isNull)
                ? MESSAGE_NOTHING_TO_SHOW
                : result.stream().filter(x -> !Objects.isNull(x)).collect(Collectors.joining("\n"));

        model.setAdditionalViewType(AdditionalViewType.TEXT);
        model.setAdditionalInfo(AdditionalInfo.of(resultString));
        return new CommandResult(feedbackMessage);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof GetCommand) {
            List<PersonalDetailRetriever> otherList = ((GetCommand) other).functionList;
            List<PersonalDetailRetriever> thisList = this.functionList;

            return thisList.stream().map(otherList::contains).reduce(true, (x, y) -> x && y)
                    &&
                    otherList.stream().map(thisList::contains).reduce(true, (x, y) -> x && y);
        }
        return false;
    }
}
