package seedu.academydirectory.versioncontrol.objects;

import java.util.Arrays;
import java.util.function.Supplier;

public class Label extends VcObject {
    public static final Label NULL = new Label("NULL", "NULL", () -> Commit.NULL);
    private final String name;
    private final Supplier<Commit> commitSupplier;

    /**
     * Creates a Label object to work with programmatically. A Label object has a reference to a Commit object
     * @param hash hash of the current Label object
     * @param commitSupplier Supplier of Commit referenced by current Label object
     */
    public Label(String hash, String name, Supplier<Commit> commitSupplier) {
        super(hash);
        this.name = name;
        this.commitSupplier = commitSupplier;
    }

    public Supplier<Commit> getCommitSupplier() {
        return commitSupplier;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (this.equals(Label.NULL)) {
            return "NULL";
        }
        return Arrays.toString(new String[]{getHash(), name});
    }
}
