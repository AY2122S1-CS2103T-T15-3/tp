package seedu.academydirectory.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_ATTRIBUTE;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_ORDER;
import static seedu.academydirectory.model.VersionedModel.PREDICATE_SHOW_ALL_STUDENTS;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import seedu.academydirectory.model.AcademyDirectory;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.student.Assessment;
import seedu.academydirectory.model.student.Name;
import seedu.academydirectory.model.student.Participation;
import seedu.academydirectory.model.student.Student;

/**
 * A class that implements the command to sort the student list.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String HELP_MESSAGE = "### Sorts students by specific attribute:  `sort`\n"
            + "Avengers will be able to sort their AcademyDirectory by the specified attribute.\n"
            + "\n"
            + "Format: `sort attr/ATTRIBUTE ord/ORDER`\n"
            + "\n"
            + "Sorts AcademyDirectory in `ORDER` order by their `ATTRIBUTE`.\n"
            + "The input `ATTRIBUTE` must be a valid attribute: name, participation,"
            + " RA1, MIDTERM, RA2, PE, FINAL, AVERAGE.\n"
            + "The input `ORDER` must be a valid order: asc, desc"
            + "\n"
            + "Example:\n"
            + "* `sort attr/name ord/asc`"
            + "* `sort attr/RA1 ord/desc`";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts student by the specified attribute. "
            + "\nParameters: "
            + PREFIX_ATTRIBUTE + "ATTRIBUTE "
            + PREFIX_ORDER + " ORDER "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_ATTRIBUTE + "name "
            + PREFIX_ORDER + "asc";

    private final String attribute;
    private final boolean isAscendingOrder;

    /**
     * Constructor for SortCommand.
     * @param attribute The attribute to be sorted by.
     * @param isAscendingOrder The order in which to sort.
     */
    public SortCommand(String attribute, boolean isAscendingOrder) {
        requireNonNull(attribute);
        requireNonNull(isAscendingOrder);
        this.attribute = attribute.toUpperCase();
        this.isAscendingOrder = isAscendingOrder;
    }

    /**
     * Return a String representation of the respective sort executed.
     * @return A String representation of the sort executed.
     */
    public String getResultString() {
        String result = "AcademyDirectory is now sorted by ";
        String orderString = isAscendingOrder ? "ascending" : "descending";
        return result + attribute + " in " + orderString + " order!";
    }

    @Override
    public CommandResult execute(VersionedModel model) {
        requireNonNull(model);

        List<Student> students = model.getAcademyDirectory().getStudentList();
        Comparator<Student> comparator = getComparator();

        List<Student> sortedList = new ArrayList<>();
        sortedList.addAll(students);
        sortedList.sort(comparator);

        String result = getResultString();
        AcademyDirectory sortedAcademyDirectory = new AcademyDirectory();
        sortedAcademyDirectory.setStudents(sortedList);
        model.setAcademyDirectory(sortedAcademyDirectory);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);

        return new CommandResult(result, Optional.of(result));
    }

    public Comparator<Student> getComparator() {
        switch(attribute) {
        case "NAME":
            return Name.getComparator(isAscendingOrder);
        case "PARTICIPATION":
            return Participation.getComparator(isAscendingOrder);
        case "AVERAGE":
            return Assessment.getAverageComparator(isAscendingOrder);
        default:
            return Assessment.getIndividualComparator(isAscendingOrder, attribute);
        }
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand e = (SortCommand) other;
        return attribute.equals(e.attribute)
                && isAscendingOrder == e.isAscendingOrder;
    }

}
