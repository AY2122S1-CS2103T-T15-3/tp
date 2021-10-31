package seedu.academydirectory.versioncontrol.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT1;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT3;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT9;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Tree;

class CommitStorageManagerTest {
    private static final Path DATA_DIR = Paths.get("src", "test",
            "data", "VersionControlTest", "StorageManagerTest");
    private static final CommitStorageManager commitStorageManager = new CommitStorageManager(
            DATA_DIR,
            new TreeStorageManager(DATA_DIR)
    );

    @Test
    public void read_allFilesPresent() {
        String filename = "CommitStorageManagerTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());

        Commit actualCommit = assertDoesNotThrow(() -> commitStorageManager.read(filename));
        assertEquals(filename, actualCommit.getHash());
        assertEquals("amadeus", actualCommit.getAuthor());
        assertEquals("17/10/2021 14:13:39", commitStorageManager.getDateFormat().format(actualCommit.getDate()));
        assertEquals("8e5abe51e5e68cc7e41e4d783529ec90506fdfa6", actualCommit.getParentSupplier().get().getHash());
        assertEquals(Tree.emptyTree(), actualCommit.getTreeSupplier().get());
    }

    @Test
    public void read_corruptedFiles_nullCommit() {
        String filename = "TreeStorageManagerTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());

        Commit actual = assertDoesNotThrow(() -> commitStorageManager.read(filename));
        assertTrue(actual.isEmpty());
    }

    @Test
    public void read_missingFile_nullCommit() {
        String filename = "IMMISSING!";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertFalse(() -> filepath.toFile().exists());

        Commit actual = assertDoesNotThrow(() -> commitStorageManager.read(filename));
        assertTrue(actual.isEmpty());
    }

    @Test
    public void getWriteableFormat_positiveTests() {
        // Missing Parent and TreeRef
        assertEquals(List.of("Author: amadeus", "Date: 31/12/1998 00:00:00", "Message: Hello, World!", "Parent: null",
                "TreeRef: null"),
                commitStorageManager.getWriteableFormat(COMMIT1));

        // Parent present but missing TreeRef
        assertEquals(List.of("Author: amadeus", "Date: 31/12/1998 00:00:00", "Message: This is Second Commit",
                        "Parent: 1",
                        "TreeRef: null"),
                commitStorageManager.getWriteableFormat(COMMIT3));

        // Parent present and TreeRef present
        assertEquals(List.of("Author: amadeus", "Date: 31/12/1998 00:00:00",
                        "Message: This is second element of linked list",
                        "Parent: 1",
                        "TreeRef: 9d34f3e9ada5ae7cc5c063b905a5d7893f792497"),
                commitStorageManager.getWriteableFormat(COMMIT9));
    }

    @Test
    public void getWriteableFormat_negativeTests() {
        // Negative Tests
        assertThrows(NullPointerException.class, () -> commitStorageManager.getWriteableFormat(null));
        assertThrows(IllegalArgumentException.class, () -> commitStorageManager.getWriteableFormat(
                Commit.emptyCommit()));
    }
}
