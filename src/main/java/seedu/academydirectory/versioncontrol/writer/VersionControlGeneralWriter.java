package seedu.academydirectory.versioncontrol.writer;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Label;
import seedu.academydirectory.versioncontrol.objects.Tree;

public class VersionControlGeneralWriter {
    private final TreeWriter treeWriter;
    private final CommitWriter commitWriter;
    private final LabelWriter labelWriter;

    /**
     * Provides a facade layer for writing {@code Tree}, {@code Commit}, and {@code Label}
     * @param vcPath Path to write files to
     */
    public VersionControlGeneralWriter(Path vcPath) {
        requireNonNull(vcPath);

        this.treeWriter = new TreeWriter(vcPath);
        this.commitWriter = new CommitWriter(vcPath);
        this.labelWriter = new LabelWriter(vcPath);
    }

    /**
     * Writes a non-empty Tree
     * @param tree Tree to be written
     */
    public void writeTree(Tree tree) throws IOException {
        if (tree.isEmpty()) {
            throw new IllegalArgumentException("Cannot write an empty tree!");
        }
        treeWriter.write(tree.getHash(), tree);
    }

    /**
     * Writes a non-empty Commit
     * @param commit Commit to be written
     */
    public void writeCommit(Commit commit) throws IOException {
        if (commit.isEmpty()) {
            throw new IllegalArgumentException("Cannot write an empty commit!");
        }
        commitWriter.write(commit.getHash(), commit);
    }

    /**
     * Writes a non-empty Label
     * @param label Label to be written
     */
    public void writeLabel(Label label) throws IOException {
        if (label.isEmpty()) {
            throw new IllegalArgumentException("Cannot write an empty label!");
        }
        labelWriter.write(label.getName(), label);
    }
}
