package seedu.academydirectory.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_TELEGRAM;
import static seedu.academydirectory.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.academydirectory.commons.core.Messages;
import seedu.academydirectory.model.Model;
import seedu.academydirectory.model.student.Information;
import seedu.academydirectory.model.student.InformationWantedFunction;

/**
 * Finds and lists all persons in academy directory whose name contains any of the argument keywords.
 * Keyword matching is case-insensitive.
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
            + "- Contact Number\n"
            + "\n"
            + "Format: `get INFORMATION [MORE INFORMATION] [STUDENT_NAME]`";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Get personal details of students "
            + "and displays them as a list\n"
            + "Parameters: " + PREFIX_EMAIL + " | "
            + PREFIX_TELEGRAM + " | " + PREFIX_PHONE + "\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_PHONE + " " + PREFIX_NAME + "Alex";

    private final List<InformationWantedFunction> filterList;

    public GetCommand(List<InformationWantedFunction> filterList) {
        this.filterList = filterList;
    }

    public GetCommand(InformationWantedFunction filter) {
        this(List.of(filter));
    }

    private String executeFilter(Model model, InformationWantedFunction filter) {
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        ObservableList<Information> view = model.getFilteredStudentListView(filter)
                .stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        return view.size() == 0
                ? String.format(Messages.MESSAGE_STUDENTS_LISTED_OVERVIEW, 0)
                : view.stream().map(Object::toString).collect(Collectors.joining("\n"));
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        return new CommandResult(filterList.stream().parallel().map(x -> executeFilter(model, x))
                .collect(Collectors.joining("\n")));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof GetCommand) {
            List<InformationWantedFunction> otherList = ((GetCommand) other).filterList;
            List<InformationWantedFunction> thisList = this.filterList;

            return thisList.stream().map(otherList::contains).reduce(true, (x, y) -> x && y)
                    &&
                    otherList.stream().map(thisList::contains).reduce(true, (x, y) -> x && y);
        }
        return false;
    }
}
