package seedu.academydirectory.versioncontrol.reader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.versioncontrol.objects.Tree;

public class TreeReaderTest {
    private static final Path DATA_DIR = Paths.get("src", "test",
            "data", "VersionControlTest", "StorageManagerTest");

    private static final TreeReader treeReader = new TreeReader(DATA_DIR);

    @Test
    public void read_fileExist_correctTree() {
        // Positive Test: File Exists and is Readable
        String filename = "TreeStorageManagerTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());

        Tree actual = assertDoesNotThrow(() -> treeReader.read(filename));
        assertEquals(filename, actual.getHash());
        String key = actual.getHashMap().keySet().toArray(String[]::new)[0];
        assertEquals("922d4ff703f9b003da1962b1a2228371718e504a", key);
        assertEquals("academydirectory.json", actual.getHashMap().get(key));
    }

    @Test
    public void read_fileAbsent_nullTree() {
        // Negative Test: File does not exist
        String missingFilename = "I'MMISSING";
        Path missingFilepath = DATA_DIR.resolve(Paths.get(missingFilename));
        assertFalse(() -> missingFilepath.toFile().exists());

        Tree actual = assertDoesNotThrow(() -> treeReader.read(missingFilename));
        assertEquals(Tree.emptyTree(), actual);
    }
}
