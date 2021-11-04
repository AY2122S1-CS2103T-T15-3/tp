package seedu.academydirectory.versioncontrol.writer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT1;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT3;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT9;
import static seedu.academydirectory.testutil.TypicalTrees.TREE1;
import static seedu.academydirectory.testutil.TypicalTrees.TREE2;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Label;
import seedu.academydirectory.versioncontrol.objects.Tree;

public class VersionControlGeneralWriterTest {
    @TempDir
    public Path tempPath;
    private VersionControlGeneralWriter versionControlGeneralWriter;

    @Test
    public void writeTree() {
        versionControlGeneralWriter = new VersionControlGeneralWriter(tempPath);

        // Correct Trees
        assertDoesNotThrow(() -> versionControlGeneralWriter.writeTree(TREE1));
        assertDoesNotThrow(() -> versionControlGeneralWriter.writeTree(TREE2));

        // Negative Test: null
        assertThrows(NullPointerException.class, () -> versionControlGeneralWriter.writeTree(null));
        assertThrows(IllegalArgumentException.class, () -> versionControlGeneralWriter.writeTree(Tree.emptyTree()));
    }

    @Test
    public void writeCommit() {
        versionControlGeneralWriter = new VersionControlGeneralWriter(tempPath);

        // Correct Trees
        assertDoesNotThrow(() -> versionControlGeneralWriter.writeCommit(COMMIT1));
        assertDoesNotThrow(() -> versionControlGeneralWriter.writeCommit(COMMIT3));
        assertDoesNotThrow(() -> versionControlGeneralWriter.writeCommit(COMMIT9));

        // Negative Test: null
        assertThrows(NullPointerException.class, () -> versionControlGeneralWriter.writeCommit(null));
        assertThrows(IllegalArgumentException.class, () -> versionControlGeneralWriter.writeCommit(
                Commit.emptyCommit()));
    }

    @Test
    public void writeLabel() {
        versionControlGeneralWriter = new VersionControlGeneralWriter(tempPath);

        // Correct Labels
        Label label1 = Label.of("Test1", "Test1", () -> COMMIT1);
        Label label2 = Label.of("Test2", "Test2", () -> COMMIT3);

        assertDoesNotThrow(() -> versionControlGeneralWriter.writeLabel(label1));
        assertDoesNotThrow(() -> versionControlGeneralWriter.writeLabel(label2));

        // Negative Test: null
        assertThrows(NullPointerException.class, () -> versionControlGeneralWriter.writeLabel(null));
        assertThrows(IllegalArgumentException.class, () -> versionControlGeneralWriter.writeLabel(
                Label.emptyLabel()));
    }

}
