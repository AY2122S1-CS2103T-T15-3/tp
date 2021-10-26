package seedu.academydirectory.ui.creator;

import java.util.Set;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import seedu.academydirectory.model.AdditionalInfo;
import seedu.academydirectory.model.student.Student;
import seedu.academydirectory.model.student.StudioRecord;
import seedu.academydirectory.model.tag.Tag;

public class ViewCreator extends Creator {

    private static final String FXML = "StudentFullInformation.fxml";

    private final Student student;

    @FXML
    private StackPane placeHolder;

    @FXML
    private Label name;

    @FXML
    private HBox tagContainer;

    @FXML
    private StackPane participation;

    @FXML
    private StackPane testScores;

    @FXML
    private Label phone;

    @FXML
    private Label email;

    @FXML
    private Label telegram;

    /**
     * View Creator for Student Detailed Information
     * @param additionalInfo information to be passed in
     */
    public ViewCreator(AdditionalInfo<?> additionalInfo) {
        super(additionalInfo, FXML);
        this.student = (Student) additionalInfo.get();
        name.setText(student.getName().fullName);
        phone.setText("- Phone number: " + student.getPhone().toString());
        email.setText("- Email address: " + student.getEmail().value);
        telegram.setText("- Telegram handle: " + student.getTelegram().value);
        Set<Tag> tags = student.getTags();
        for (Tag tag : tags) {
            CheckBox checkBox = new CheckBox(tag.toString());
            tagContainer.getChildren().add(checkBox);
        }
        StudioRecord studioRecord = student.getStudioRecord();
        Label displayInfoPlaceHolder = new Label(studioRecord.getExtendedStudioRecords());
        participation.getChildren().add(displayInfoPlaceHolder);
        String assessments = student.getAssessment().getVisualizerDisplay();
        testScores.getChildren().add(new Label(assessments));
    }

    @Override
    public Node create() {
        return this.getRoot();
    }
}
