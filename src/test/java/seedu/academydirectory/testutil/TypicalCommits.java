package seedu.academydirectory.testutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Tree;

/**
 * A utility class containing a list of {@code Commit} objects to be used in tests.
 */
public class TypicalCommits {
    private static final Date date;

    static {
        Date date1;
        try {
            date1 = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998");
        } catch (ParseException e) {
            date1 = null;
            e.printStackTrace();
        }
        date = date1;
    }

    public static final Commit COMMIT1 = new CommitBuilder()
            .withHash("123456789ThisIsFun!")
            .withAuthor("Alice Pauline")
            .withDate(date)
            .withMessage("Hello, World!")
            .withParentSupplier(() -> Commit.NULL)
            .withTreeSupplier(() -> Tree.NULL)
            .build();

    public static List<Commit> getTypicalCommits() {
        return new ArrayList<>(List.of(COMMIT1));
    }
}
