package seedu.academydirectory.versioncontrol.reader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Tree;

public class CommitReaderTest {
    private static final Path DATA_DIR = Paths.get("src", "test",
            "data", "VersionControlTest", "StorageManagerTest");
    private static final CommitReader commitReader = new CommitReader(
            DATA_DIR,
            new TreeReader(DATA_DIR)
    );

    @Test
    public void read_commitExist_correctCommit() {
        String filename = "CommitStorageManagerTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());

        Commit actualCommit = assertDoesNotThrow(() -> commitReader.read(filename));
        assertEquals(filename, actualCommit.getHash());
        assertEquals("amadeus", actualCommit.getAuthor());
        assertEquals("17/10/2021 14:13:39", commitReader.getDateFormat().format(actualCommit.getDate()));
        assertEquals("8e5abe51e5e68cc7e41e4d783529ec90506fdfa6", actualCommit.getParentSupplier().get().getHash());
        assertEquals(Tree.emptyTree(), actualCommit.getTreeSupplier().get());
    }

    @Test
    public void read_commitCorrupted_nullCommit() {
        // Completely cannot understand anything
        String filename = "TreeStorageManagerTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());

        Commit actual = assertDoesNotThrow(() -> commitReader.read(filename));
        assertTrue(actual.isEmpty());

        // Can understand everything except for date
        String wrongFilename = "WrongDateTest";
        Path wrongFilepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> wrongFilepath.toFile().exists());

        actual = assertDoesNotThrow(() -> commitReader.read(wrongFilename));
        assertTrue(actual.isEmpty());
    }

    @Test
    public void read_commitAbsent_nullCommit() {
        String filename = "IMMISSING!";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertFalse(() -> filepath.toFile().exists());

        Commit actual = assertDoesNotThrow(() -> commitReader.read(filename));
        assertTrue(actual.isEmpty());
    }
}
