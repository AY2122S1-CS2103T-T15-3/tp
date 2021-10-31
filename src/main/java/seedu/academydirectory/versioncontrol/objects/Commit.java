package seedu.academydirectory.versioncontrol.objects;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

public class Commit extends VcObject {
    private static final Commit NULL = new Commit(null, null, null,
            "", Commit::emptyCommit, Tree::emptyTree);

    // Commit specific objects
    private final String author;
    private final Date date;
    private final String message;

    // References
    private final Supplier<Commit> parentSupplier;
    private final Supplier<Tree> treeSupplier;

    private Commit(String hash, String author, Date date, String message, Supplier<Commit> parentSupplier,
                  Supplier<Tree> treeSupplier) {
        super(hash);
        this.author = author;
        this.date = date;
        this.message = message.substring(0, Math.min(message.length(), 128));
        this.parentSupplier = parentSupplier;
        this.treeSupplier = treeSupplier;
    }

    /**
     * Creates a Commit object to work with programmatically. A Commit object SHOULD NOT be instantiated directly.
     * Instead, use a CommitController object to ensure all Commit objects follow the established contract
     * @param message commit message. May be omitted
     * @param date date and time when Commit object is created
     * @param hash commit hash
     * @param parentSupplier  supplier to parent Commit that current Commit object is pointing to
     * @param treeSupplier supplier to Tree referenced by current Commit object
     */
    public static Commit of(String hash, String author, Date date, String message, Supplier<Commit> parentSupplier,
                            Supplier<Tree> treeSupplier) {
        requireNonNull(hash);
        requireNonNull(author);
        requireNonNull(date);
        requireNonNull(message);
        requireNonNull(parentSupplier);
        requireNonNull(treeSupplier);
        return new Commit(hash, author, date, message, parentSupplier, treeSupplier);
    }

    private Commit copy() {
        if (this.isEmpty()) {
            return emptyCommit();
        }
        return new Commit(this.getHash(), this.getAuthor(), this.getDate(), this.getMessage(),
                this.parentSupplier, this.treeSupplier);
    }

    public static Commit emptyCommit() {
        return NULL;
    }

    public boolean isEmpty() {
        return this == Commit.NULL;
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "NULL";
        }
        return Arrays.toString(new String[]{getHash(), author, date.toString(), message});
    }

    public String getAuthor() {
        return author;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public Supplier<Commit> getParentSupplier() {
        return parentSupplier;
    }

    public Supplier<Tree> getTreeSupplier() {
        return treeSupplier;
    }

    /**
     * Retrieve all the ancestors of the current Commit object
     * @return ancestors of current commit object, including the current given Commit object
     */
    public List<Commit> getHistory() {
        return getHistory(Commit.NULL);
    }

    /**
     * Retrieve all the ancestors of the current Commit object, up to an end Commit object. If end Commit object is an
     * ancestor, then all Commit objects up to but excluding the end Commit object will be included. Otherwise,
     * all ancestor Commit objects are returned.
     * @param endExclusive end Commit object
     * @return ancestors of the current commit object, including current Commit object but excluding the end Commit
     */
    public List<Commit> getHistory(Commit endExclusive) {
        if (this.isEmpty()) {
            return new ArrayList<>();
        }
        return this.copy().getHistoryNoCopy(endExclusive);
    }

    private List<Commit> getHistoryNoCopy(Commit endExclusive) {
        Commit commit = this;
        List<Commit> history = new ArrayList<>();
        if (commit.isEmpty() || commit.equals(endExclusive)) {
            return new ArrayList<>();
        }

        history.add(commit);
        Supplier<Commit> parentCommitSupplier = commit.getParentSupplier();
        while (!parentCommitSupplier.get().equals(endExclusive)) {
            commit = parentCommitSupplier.get();
            history.add(commit);
            parentCommitSupplier = commit.getParentSupplier();
        }
        return history;
    }

    /**
     * Return the lowest common ancestor
     * @param otherCommit Other Commit object
     * @return Lowest common ancestor of both commit objects.
     */
    public Commit findLca(Commit otherCommit) {
        return this.copy().findLcaNoCopy(otherCommit.copy());
    }

    private Commit findLcaNoCopy(Commit otherCommit) {
        requireNonNull(otherCommit);

        if (this.isEmpty() || otherCommit.isEmpty()) {
            return Commit.NULL;
        }

        List<Commit> fromA = this.getHistory();
        List<Commit> fromB = otherCommit.getHistory();

        int shiftDepth = Math.abs(fromA.size() - fromB.size());
        Commit toMove = fromA.size() > fromB.size() ? this : otherCommit;
        Commit notMoved = fromA.size() > fromB.size() ? otherCommit : this;
        Commit afterMove = toMove.move(shiftDepth);

        while (!notMoved.equals(afterMove)) {
            notMoved = notMoved.move(1);
            afterMove = afterMove.move(1);
        }
        assert notMoved.equals(afterMove);
        return notMoved;
    }

    /**
     * Return the ancestor that is the furthest away, limited by the given end Commit.
     * @param endExclusive Commit object which limits the search
     * @return furthest ancestor of queriedCommit but is child of endExclusive
     */
    public Commit getHighestAncestor(Commit endExclusive) {
        return this.copy().getHighestAncestorNoCopy(endExclusive);
    }

    private Commit getHighestAncestorNoCopy(Commit endExclusive) {
        if (this.isEmpty() || this.getParentSupplier().get().equals(endExclusive)) {
            return this;
        }
        return this.getParentSupplier().get().getHighestAncestorNoCopy(endExclusive);
    }

    private Commit move(int numStep) {
        return this.copy().moveNoCopy(numStep);
    }

    private Commit moveNoCopy(int numStep) {
        if (this.isEmpty() || numStep == 0) {
            return this;
        }
        return this.getParentSupplier().get().moveNoCopy(numStep - 1);
    }
}
