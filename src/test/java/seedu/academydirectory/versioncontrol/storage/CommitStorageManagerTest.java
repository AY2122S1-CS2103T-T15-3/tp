package seedu.academydirectory.versioncontrol.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.versioncontrol.objects.Commit;

class CommitStorageManagerTest {
    private static final Path DATA_DIR = Paths.get("src", "test",
            "data", "VersionControlTest", "ParserTest");

    @Test
    public void read_allFilesPresent() {
        CommitStorageManager commitStorageManager = new CommitStorageManager(DATA_DIR,
                new TreeStorageManager(DATA_DIR));

        String filename = "CommitParserTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());

        Commit actualCommit = assertDoesNotThrow(() -> commitStorageManager.read(filename));
        assertEquals(filename, actualCommit.getHash());
        assertEquals("amadeus", actualCommit.getAuthor());
        assertEquals("17/10/2021 14:13:39", commitStorageManager.getDateFormat().format(actualCommit.getDate()));
        assertEquals("8e5abe51e5e68cc7e41e4d783529ec90506fdfa6", actualCommit.getParentSupplier().get().getHash());
        assertEquals("18f30e1a6e30583a2a8afd86c08b06c449658db1", actualCommit.getTreeSupplier().get().getHash());
    }

    @Test
    public void read_corruptedFiles_nullCommit() {
        CommitStorageManager commitStorageManager = new CommitStorageManager(DATA_DIR,
                new TreeStorageManager(DATA_DIR));

        String filename = "TreeParserTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());

        Commit actual = assertDoesNotThrow(() -> commitStorageManager.read(filename));
        assertEquals(Commit.NULL, actual);
    }

    @Test
    public void read_missingFile_nullCommit() {
        CommitStorageManager commitStorageManager = new CommitStorageManager(DATA_DIR,
                new TreeStorageManager(DATA_DIR));

        String filename = "IMMISSING!";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertFalse(() -> filepath.toFile().exists());

        Commit actual = assertDoesNotThrow(() -> commitStorageManager.read(filename));
        assertEquals(Commit.NULL, actual);
    }
}
