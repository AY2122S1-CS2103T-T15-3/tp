package seedu.academydirectory.testutil;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Supplier;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Tree;


/**
 * A utility class to help with building Commit objects
 */
public class CommitBuilder {

    public static final String DEFAULT_HASH = "";
    public static final String DEFAULT_AUTHOR = "";
    public static final Date DEFAULT_DATE = assertDoesNotThrow(() -> new SimpleDateFormat("dd/MM/yyyy")
            .parse("31/12/1998"));
    public static final String DEFAULT_MESSAGE = "Hello, World!";
    public static final Supplier<Commit> DEFAULT_PARENT_SUPPLIER = Commit::emptyCommit;
    public static final Supplier<Tree> DEFAULT_TREE_SUPPLIER = Tree::emptyTree;

    private String hash;
    private String author;
    private Date date;
    private String message;
    private Supplier<Commit> parentSupplier;
    private Supplier<Tree> treeSupplier;

    /**
     * Creates a {@Code CommitController} with the default details
     */
    public CommitBuilder() {
        hash = DEFAULT_HASH;
        author = DEFAULT_AUTHOR;
        date = DEFAULT_DATE;
        message = DEFAULT_MESSAGE;
        parentSupplier = DEFAULT_PARENT_SUPPLIER;
        treeSupplier = DEFAULT_TREE_SUPPLIER;
    }

    /**
     * Initializes the CommitController with the data of {@code commitToCopy}.
     */
    public CommitBuilder(Commit commitToCopy) {
        hash = commitToCopy.getHash();
        author = commitToCopy.getAuthor();
        date = commitToCopy.getDate();
        message = commitToCopy.getMessage();
        parentSupplier = commitToCopy.getParentSupplier();
        treeSupplier = commitToCopy.getTreeSupplier();
    }

    /**
     * Sets the {@code hash} of the {@code Commit} that we are building.
     */
    public CommitBuilder withHash(String hash) {
        this.hash = hash;
        return this;
    }

    /**
     * Sets the {@code author} of the {@code Commit} that we are building.
     */
    public CommitBuilder withAuthor(String author) {
        this.author = author;
        return this;
    }

    /**
     * Sets the {@code date} of the {@code Commit} that we are building.
     */
    public CommitBuilder withDate(Date date) {
        this.date = date;
        return this;
    }

    /**
     * Sets the {@code message} of the {@code Commit} that we are building.
     */
    public CommitBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Sets the {@code parentSupplier} of the {@code Commit} that we are building.
     */
    public CommitBuilder withParentSupplier(Supplier<Commit> parentSupplier) {
        this.parentSupplier = parentSupplier;
        return this;
    }

    /**
     * Sets the {@code treeSupplier} of the {@code Commit} that we are building.
     */
    public CommitBuilder withTreeSupplier(Supplier<Tree> treeSupplier) {
        this.treeSupplier = treeSupplier;
        return this;
    }

    /**
     * Builds the Commit object for testing.
     */
    public Commit build() {
        return Commit.of(hash, author, date, message, parentSupplier, treeSupplier);
    }
}
