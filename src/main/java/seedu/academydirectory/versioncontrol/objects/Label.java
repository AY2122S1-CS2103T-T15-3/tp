package seedu.academydirectory.versioncontrol.objects;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.function.Supplier;

public class Label extends VcObject {
    private static final Label NULL = new Label(null, null, Commit::emptyCommit);
    private final String name;
    private final Supplier<Commit> commitSupplier;

    private Label(String hash, String name, Supplier<Commit> commitSupplier) {
        super(hash);
        this.name = name;
        this.commitSupplier = commitSupplier;
    }

    /**
     * Creates a Label object to work with programmatically. A Label object has a reference to a Commit object
     * @param hash hash of the current Label object
     * @param commitSupplier Supplier of Commit referenced by current Label object
     */
    public static Label of(String hash, String name, Supplier<Commit> commitSupplier) {
        requireNonNull(hash);
        requireNonNull(name);
        requireNonNull(commitSupplier);
        return new Label(hash, name, commitSupplier);
    }

    public Supplier<Commit> getCommitSupplier() {
        return commitSupplier;
    }

    public String getName() {
        return name;
    }

    public boolean isEmpty() {
        return this == NULL;
    }

    public static Label emptyLabel() {
        return NULL;
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "NULL";
        }
        return Arrays.toString(new String[]{getHash(), name});
    }
}
