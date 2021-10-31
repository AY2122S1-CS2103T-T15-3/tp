package seedu.academydirectory.versioncontrol.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.academydirectory.testutil.TypicalTrees.TREE1;
import static seedu.academydirectory.testutil.TypicalTrees.TREE2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.versioncontrol.objects.Tree;

public class TreeStorageManagerTest {
    private static final Path DATA_DIR = Paths.get("src", "test",
            "data", "VersionControlTest", "StorageManagerTest");

    private static final TreeStorageManager treeStorageManager = new TreeStorageManager(DATA_DIR);

    @Test
    public void read_fileExist_correctTree() {
        // Positive Test: File Exists and is Readable
        String filename = "TreeStorageManagerTest";
        Path filepath = DATA_DIR.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());

        Tree actual = assertDoesNotThrow(() -> treeStorageManager.read(filename));
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

        Tree actual = assertDoesNotThrow(() -> treeStorageManager.read(missingFilename));
        assertEquals(Tree.emptyTree(), actual);
    }

    @Test
    public void read_fileCorrupted_nullTree() {
        // Negative Test: File Exists but is not understandable
        String commitFilename = "CommitStorageManagerTest";
        Path commitFilepath = DATA_DIR.resolve(Paths.get(commitFilename));
        assertTrue(() -> commitFilepath.toFile().exists());

        Tree actual = assertDoesNotThrow(() -> treeStorageManager.read(commitFilename));
        assertEquals(Tree.emptyTree(), actual);
    }

    @Test
    public void getWriteableFormat() {
        TreeStorageManager treeStorageManager = new TreeStorageManager(DATA_DIR);

        // Correct Trees
        assertEquals(List.of("world.hello hello.world"), treeStorageManager.getWriteableFormat(TREE1));
        assertEquals(List.of("world_hello Hello.png", "world_hello? Hello World.java"),
                treeStorageManager.getWriteableFormat(TREE2));

        // Negative Test: null
        assertThrows(NullPointerException.class, () -> treeStorageManager.getWriteableFormat(null));
        assertThrows(IllegalArgumentException.class, () -> treeStorageManager.getWriteableFormat(Tree.emptyTree()));
    }
}
