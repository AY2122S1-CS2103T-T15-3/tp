package seedu.academydirectory.ui.creator;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import seedu.academydirectory.model.AdditionalInfo;

public class HistoryCreator extends Creator {

    private static final String FXML = "creator/HistoryCreator.fxml";

    @FXML
    private TextArea resultDisplay;

    /**
     * Constructor of a History creator
     *
     * @param additionalInfo information to be passed in
     */
    public HistoryCreator(AdditionalInfo<?> additionalInfo) {
        super(additionalInfo, FXML);
        String history = (String) additionalInfo.get();

        resultDisplay.setWrapText(true);
        resultDisplay.setText(history);
        resultDisplay.setPrefRowCount(30);
    }

    @Override
    public Node create() {
        return getRoot();
    }
}
