package seedu.academydirectory.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.logic.AdditionalViewType;

public class AdditionalViewModelTest {

    @Test
    public void testValidModel() {
        AdditionalViewModel additionalViewModel1 = new AdditionalViewModel(
                AdditionalViewType.VIEW, AdditionalInfo.of("View")
        );

        // additional view model is not null
        assertNotEquals(additionalViewModel1, null);

        AdditionalViewModel additionalViewModel2 = new AdditionalViewModel(
                AdditionalViewType.TEXT, AdditionalInfo.of(3.5)
        );

        // two additional view models are equal if
        // the view type and info are equal
        assertEquals(additionalViewModel2, new AdditionalViewModel(
                AdditionalViewType.TEXT, AdditionalInfo.of(3.5)
        ));

        // they are not equal otherwise
        assertNotEquals(additionalViewModel1, additionalViewModel2);

        AdditionalViewModel additionalViewModel3 = new AdditionalViewModel(
                AdditionalViewType.DEFAULT, AdditionalInfo.empty()
        );

        // two additional view model with empty info are equal
        assertEquals(additionalViewModel3, new AdditionalViewModel(
                AdditionalViewType.DEFAULT, AdditionalInfo.empty()
        ));

        // same type of view but different additional info
        assertNotEquals(additionalViewModel3, new AdditionalViewModel(
                AdditionalViewType.DEFAULT, AdditionalInfo.of("")
        ));

        // same type of additional info but different view
        assertNotEquals(additionalViewModel3, new AdditionalViewModel(
                AdditionalViewType.TEXT, AdditionalInfo.empty()
        ));
    }

    @Test
    public void testSetterAndGetter() {
        AdditionalViewModel additionalViewModel = new AdditionalViewModel(
                AdditionalViewType.DEFAULT, AdditionalInfo.empty()
        );

        // get additional view type is correct
        assertEquals(additionalViewModel.getAdditionalViewType(), AdditionalViewType.DEFAULT);

        // get additional info is correct
        assertEquals(additionalViewModel.getAdditionalInfo(), AdditionalInfo.empty());

        // set view model type for empty info works properly
        additionalViewModel.setAdditionalViewType(AdditionalViewType.VIEW);
        assertEquals(additionalViewModel.getAdditionalViewType(), AdditionalViewType.VIEW);

        // set additional info works properly
        additionalViewModel.setAdditionalInfo(AdditionalInfo.of("Tyrion Lannister"));
        assertEquals(additionalViewModel.getAdditionalInfo(), AdditionalInfo.of("Tyrion Lannister"));
    }
}
