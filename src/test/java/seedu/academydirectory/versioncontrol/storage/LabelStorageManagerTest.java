package seedu.academydirectory.versioncontrol.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.versioncontrol.objects.Label;

public class LabelStorageManagerTest {
    private static final Path DATA_DIR = Paths.get("src", "test",
            "data", "VersionControlTest", "StorageManagerTest");

    @Test
    public void read_fileExist_correctLabel() {
        TreeStorageManager treeStorageManager = new TreeStorageManager(DATA_DIR);
        CommitStorageManager commitStorageManager = new CommitStorageManager(DATA_DIR, treeStorageManager);
        LabelStorageManager labelStorageManager = new LabelStorageManager(DATA_DIR, commitStorageManager);

        // Positive Test: Label present but labelled commit is absent
        String filename = "HeadLabel_NoRef";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        Path finalFilepath = filepath;
        assertTrue(() -> finalFilepath.toFile().exists());

        Label label = labelStorageManager.read(filename);
        assertFalse(label.isEmpty()); // Should not be Null, since reading is successful
        assertTrue(label.getCommitSupplier().get().isEmpty()); // Commit is null, since labelled commit is missing

        // Positive Test: Label present and labelled commit is present
        filename = "HeadLabel_WithRef";
        filepath = DATA_DIR.resolve(Paths.get(filename));
        Path finalFilepath1 = filepath;
        assertTrue(() -> finalFilepath1.toFile().exists());

        label = labelStorageManager.read(filename);
        assertFalse(label.isEmpty());
        assertEquals("CommitStorageManagerTest", label.getCommitSupplier().get().getHash());
    }

    @Test
    public void read_fileAbsent_labelNull() {
        TreeStorageManager treeStorageManager = new TreeStorageManager(DATA_DIR);
        CommitStorageManager commitStorageManager = new CommitStorageManager(DATA_DIR, treeStorageManager);
        LabelStorageManager labelStorageManager = new LabelStorageManager(DATA_DIR, commitStorageManager);

        String filename = "NOSUCHFILE";
        Path filepath = Paths.get("src", "test", "data", "VersionControlTest", "ParserTest", filename);
        assertFalse(() -> filepath.toFile().exists());
        assertTrue(labelStorageManager.read(filename).isEmpty());
    }

    @Test
    public void read_fileCorrupt_labelNull() {
        TreeStorageManager treeStorageManager = new TreeStorageManager(DATA_DIR);
        CommitStorageManager commitStorageManager = new CommitStorageManager(DATA_DIR, treeStorageManager);
        LabelStorageManager labelStorageManager = new LabelStorageManager(DATA_DIR, commitStorageManager);

        String filename = "CommitStorageManagerTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());
        assertTrue(labelStorageManager.read(filename).isEmpty());
    }
}
