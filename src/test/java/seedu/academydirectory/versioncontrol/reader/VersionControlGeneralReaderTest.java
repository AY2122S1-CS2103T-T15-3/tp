package seedu.academydirectory.versioncontrol.reader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Label;
import seedu.academydirectory.versioncontrol.objects.Tree;

/**
 * Contains integration tests (interaction with the VersionControlObjectReaders).
 */
public class VersionControlGeneralReaderTest {
    private static final Path DATA_DIR = Paths.get("src", "test",
            "data", "VersionControlTest", "StorageManagerTest");
    private static final VersionControlGeneralReader versionControlGeneralReader =
            new VersionControlGeneralReader(DATA_DIR);

    // Tests reading trees
    @Test
    public void read_treeExist_correctTree() {
        TreeReader treeReader = new TreeReader(DATA_DIR);

        // Positive Test: File Exists and is Readable
        String filename = "TreeStorageManagerTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());

        Tree expected = assertDoesNotThrow(() -> treeReader.read(filename));
        Tree actual = assertDoesNotThrow(() -> versionControlGeneralReader.readTree(filename));

        assertEquals(expected, actual);
    }

    @Test
    public void read_treeAbsent_nullTree() {
        // Negative Test: File does not exist
        String missingFilename = "I'MMISSING";
        Path missingFilepath = DATA_DIR.resolve(Paths.get(missingFilename));
        assertFalse(() -> missingFilepath.toFile().exists());

        Tree actual = assertDoesNotThrow(() -> versionControlGeneralReader.readTree(missingFilename));
        assertEquals(Tree.emptyTree(), actual);
    }

    // Tests reading commits
    @Test
    public void read_commitExist_correctCommit() {
        CommitReader commitReader = new CommitReader(DATA_DIR, new TreeReader(DATA_DIR));

        String filename = "CommitStorageManagerTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());

        Commit actualCommit = assertDoesNotThrow(() -> versionControlGeneralReader.readCommit(filename));
        Commit expectedCommit = assertDoesNotThrow(() -> commitReader.read(filename));
        assertEquals(expectedCommit, actualCommit);
    }

    @Test
    public void read_commitCorrupted_nullCommit() {
        String filename = "TreeStorageManagerTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());

        Commit actualCommit = assertDoesNotThrow(() -> versionControlGeneralReader.readCommit(filename));
        assertTrue(actualCommit.isEmpty());
    }

    @Test
    public void read_commitAbsent_nullCommit() {
        String filename = "IMMISSING";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertFalse(() -> filepath.toFile().exists());

        Commit actualCommit = assertDoesNotThrow(() -> versionControlGeneralReader.readCommit(filename));
        assertTrue(actualCommit.isEmpty());
    }

    // Tests reading labels
    @Test
    public void read_labelExist_correctLabel() {
        LabelReader labelReader = new LabelReader(DATA_DIR,
                new CommitReader(DATA_DIR, new TreeReader(DATA_DIR)));

        // Positive Test: Label present but labelled commit is absent
        String filename = "HeadLabel_NoRef";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        Path finalFilepath = filepath;
        assertTrue(() -> finalFilepath.toFile().exists());

        Label expectedLabel = labelReader.read(filename);
        Label actualLabel = versionControlGeneralReader.readLabel(filename);
        assertEquals(expectedLabel, actualLabel);
        assertFalse(actualLabel.isEmpty()); // Should not be Null, since reading is successful
        assertTrue(actualLabel.getCommitSupplier().get().isEmpty()); // Commit is null, since labelled commit is missing

        // Positive Test: Label present and labelled commit is present
        filename = "HeadLabel_WithRef";
        filepath = DATA_DIR.resolve(Paths.get(filename));
        Path finalFilepath1 = filepath;
        assertTrue(() -> finalFilepath1.toFile().exists());

        expectedLabel = labelReader.read(filename);
        actualLabel = versionControlGeneralReader.readLabel(filename);
        assertEquals(expectedLabel, actualLabel);
        assertFalse(actualLabel.isEmpty());
        assertEquals("CommitStorageManagerTest", actualLabel.getCommitSupplier().get().getHash());
    }

    @Test
    public void read_labelAbsent_nullLabel() {
        String filename = "NOSUCHFILE";
        Path filepath = Paths.get("src", "test", "data", "VersionControlTest", "ParserTest", filename);
        assertFalse(() -> filepath.toFile().exists());
        assertTrue(versionControlGeneralReader.readLabel(filename).isEmpty());
    }

    @Test
    public void read_labelCorrupted_nullLabel() {
        String filename = "CommitStorageManagerTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());
        assertTrue(versionControlGeneralReader.readLabel(filename).isEmpty());
    }
}
