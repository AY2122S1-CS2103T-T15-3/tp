package seedu.academydirectory.ui;

import java.util.Comparator;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.academydirectory.commons.core.LogsCenter;
import seedu.academydirectory.model.student.Student;

/**
 * An UI component that displays information of a {@code Student}.
 */
public class StudentCard extends UiPart<Region> {

    private static final String FXML = "StudentListCard.fxml";
    private final Logger logger = LogsCenter.getLogger(getClass());

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AcademyDirectory level 4</a>
     */
    private final Student student;

    @FXML
    private HBox cardPane;
    @FXML
    private VBox container;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label email;
    @FXML
    private Label telegram;
    @FXML
    private Label studioRecord;
    @FXML
    private Label assessment;
    @FXML
    private FlowPane tags;

    private int displayedIndex;
    private Label temp;
    private CommandBox commandBox;

    /**
     * Creates a {@code StudentCode} with the given {@code Student} and index to display.
     * @param student student to be displayed
     * @param displayedIndex index of the student
     */
    public StudentCard(Student student, int displayedIndex, CommandBox commandBox) {
        super(FXML);
        this.student = student;
        this.displayedIndex = displayedIndex;
        id.setText(displayedIndex + ". ");
        name.setText(student.getName().fullName);
        phone.setText(student.getPhone().toString());
        email.setText(student.getEmail().value);
        studioRecord.setText(student.getStudioRecord().toString());
        telegram.setText(student.getTelegram().value);
        assessment.setText(student.getAssessment().toString());
        student.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
        temp = new Label("Clicked");
        this.commandBox = commandBox;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof StudentCard)) {
            return false;
        }

        // state check
        StudentCard card = (StudentCard) other;
        return id.getText().equals(card.id.getText())
                && student.equals(card.student);
    }

    /**
     * View full information of the student in mention
     */
    @FXML
    public void viewFullInformation() {
        logger.info("Item selected");
        String commandEquivalent = "view " + this.displayedIndex;
        commandBox.execute(commandEquivalent);
    }
}
