package seedu.academydirectory.versioncontrol.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.versioncontrol.objects.Label;

public class LabelReaderTest {
    private static final Path DATA_DIR = Paths.get("src", "test",
            "data", "VersionControlTest", "StorageManagerTest");
    private static final LabelReader labelReader = new LabelReader(
            DATA_DIR,
            new CommitReader(DATA_DIR, new TreeReader(DATA_DIR)));

    @Test
    public void read_labelExist_correctLabel() {
        // Positive Test: Label present but labelled commit is absent
        String filename = "HeadLabel_NoRef";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        Path finalFilepath = filepath;
        assertTrue(() -> finalFilepath.toFile().exists());

        Label label = labelReader.read(filename);
        assertFalse(label.isEmpty()); // Should not be Null, since reading is successful
        assertTrue(label.getCommitSupplier().get().isEmpty()); // Commit is null, since labelled commit is missing

        // Positive Test: Label present and labelled commit is present
        filename = "HeadLabel_WithRef";
        filepath = DATA_DIR.resolve(Paths.get(filename));
        Path finalFilepath1 = filepath;
        assertTrue(() -> finalFilepath1.toFile().exists());

        label = labelReader.read(filename);
        assertFalse(label.isEmpty());
        assertEquals("CommitStorageManagerTest", label.getCommitSupplier().get().getHash());
    }

    @Test
    public void read_labelAbsent_nullLabel() {
        String filename = "NOSUCHFILE";
        Path filepath = Paths.get("src", "test", "data", "VersionControlTest", "ParserTest", filename);
        assertFalse(() -> filepath.toFile().exists());
        assertTrue(labelReader.read(filename).isEmpty());
    }

    @Test
    public void read_labelCorrupted_nullLabel() {
        String filename = "CommitStorageManagerTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());
        assertTrue(labelReader.read(filename).isEmpty());
    }
}
