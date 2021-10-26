package seedu.academydirectory.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import seedu.academydirectory.model.AdditionalViewModel;
import seedu.academydirectory.ui.creator.Creator;
import seedu.academydirectory.ui.creator.DefaultCreator;
import seedu.academydirectory.ui.creator.GraphCreator;
import seedu.academydirectory.ui.creator.HistoryCreator;
import seedu.academydirectory.ui.creator.ViewCreator;

/**
 * A ui for the visualiser bar that is displayed at bottom right of the app.
 */
public class VisualiserDisplay extends UiPart<Region> {
    private static final String FXML = "VisualizeDisplay.fxml";

    @FXML
    private StackPane placeHolder;

    public VisualiserDisplay() {
        super(FXML);
    }

    private void setVisualizer(Creator creator) {
        placeHolder.getChildren().clear();
        placeHolder.getChildren().add(creator.create());
    }

    /**
     * Handle type of AdditionalView being passed inside AD
     * @param additionalViewModel additional View model
     */
    public void handleAdditionalInfo(AdditionalViewModel additionalViewModel) {
        switch (additionalViewModel.getAdditionalViewType()) {
        case VIEW:
            setVisualizer(new ViewCreator(additionalViewModel.getAdditionalInfo()));
            break;
        case VISUALISE:
            setVisualizer(new GraphCreator(additionalViewModel.getAdditionalInfo()));
            break;
        case HISTORY:
            setVisualizer(new HistoryCreator(additionalViewModel.getAdditionalInfo()));
            break;
        default:
            setVisualizer(new DefaultCreator(additionalViewModel.getAdditionalInfo()));
            break;
        }
    }
}
