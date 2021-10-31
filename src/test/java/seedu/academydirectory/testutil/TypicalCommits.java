package seedu.academydirectory.testutil;

import static seedu.academydirectory.testutil.TypicalTrees.TREE1;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Label;
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

    // Standalone Commit with no child
    public static final Commit COMMIT1 = new CommitBuilder()
            .withHash("1d83638a25901e76c8e3882afca2347f8352cd06")
            .withAuthor("Alice Pauline")
            .withDate(date)
            .withMessage("Hello, World!")
            .withParentSupplier(Commit::emptyCommit)
            .withTreeSupplier(Tree::emptyTree)
            .build();

    // Commit Chain: Tree form
    public static final Commit COMMIT2 = new CommitBuilder()
            .withHash("1")
            .withAuthor("Alice")
            .withDate(date)
            .withMessage("This is Initial Commit")
            .withParentSupplier(Commit::emptyCommit)
            .withTreeSupplier(Tree::emptyTree)
            .build();

    public static final Commit COMMIT3 = new CommitBuilder()
            .withHash("2")
            .withAuthor("Alice")
            .withDate(date)
            .withMessage("This is Second Commit")
            .withParentSupplier(() -> COMMIT2)
            .withTreeSupplier(Tree::emptyTree)
            .build();

    public static final Commit COMMIT4 = new CommitBuilder()
            .withHash("3")
            .withAuthor("Alice")
            .withDate(date)
            .withMessage("This is third Commit")
            .withParentSupplier(() -> COMMIT2)
            .withTreeSupplier(Tree::emptyTree)
            .build();

    public static final Commit COMMIT5 = new CommitBuilder()
            .withHash("4")
            .withAuthor("Alice")
            .withDate(date)
            .withMessage("This is fourth Commit")
            .withParentSupplier(() -> COMMIT4)
            .withTreeSupplier(Tree::emptyTree)
            .build();

    // Commit Chain: LinkedList form
    public static final Commit COMMIT6 = new CommitBuilder()
            .withHash("6")
            .withAuthor("Bob")
            .withDate(date)
            .withMessage("This is root of linked list")
            .withParentSupplier(Commit::emptyCommit)
            .withTreeSupplier(Tree::emptyTree)
            .build();

    public static final Commit COMMIT7 = new CommitBuilder()
            .withHash("7")
            .withAuthor("Bob")
            .withDate(date)
            .withMessage("This is first element of linked list")
            .withParentSupplier(() -> COMMIT6)
            .withTreeSupplier(Tree::emptyTree)
            .build();

    public static final Commit COMMIT8 = new CommitBuilder()
            .withHash("8")
            .withAuthor("Bob")
            .withDate(date)
            .withMessage("This is second element of linked list")
            .withParentSupplier(() -> COMMIT7)
            .withTreeSupplier(Tree::emptyTree)
            .build();

    public static final Commit COMMIT9 = new CommitBuilder()
            .withHash("9")
            .withAuthor("Bob")
            .withDate(date)
            .withMessage("This is second element of linked list")
            .withParentSupplier(() -> COMMIT2)
            .withTreeSupplier(() -> TREE1)
            .build();

    public static List<Commit> getTypicalCommits() {
        return new ArrayList<>(List.of(COMMIT1, COMMIT3, COMMIT9));
    }

    public static List<Commit> getTypicalCommitList() {
        return new ArrayList<>(List.of(COMMIT8, COMMIT7, COMMIT6));
    }

    public static List<Commit> getTypicalCommitTree() {
        return new ArrayList<>(List.of(COMMIT5, COMMIT2));
    }

    public static List<Label> getTypicalLabels() {
        return getTypicalCommits().stream()
                .map(x -> Label.of("Label" + x.getHash(), "Label" + x.getHash(), () -> x))
                .collect(Collectors.toList());
    }

    public static List<Label> getTypicalLabelList() {
        return getTypicalCommitList().stream()
                .map(x -> Label.of("Label" + x.getHash(), "Label" + x.getHash(), () -> x))
                .collect(Collectors.toList());
    }

    public static List<Label> getTypicalLabelTree() {
        return getTypicalCommitTree().stream()
                .map(x -> Label.of("Label" + x.getHash(), "Label" + x.getHash(), () -> x))
                .collect(Collectors.toList());
    }
}
