package seedu.academydirectory.versioncontrol.objects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.academydirectory.testutil.TypicalTrees.TREE1;
import static seedu.academydirectory.testutil.TypicalTrees.TREE2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.academydirectory.commons.util.FileUtil;

public class TreeTest {
    @TempDir
    public Path tempPath;

    @Test
    public void equals() {
        // same values -> returns true
        Tree tree1Copy = Tree.of(TREE1.getHash(), "hello.world", "world.hello");
        assertEquals(TREE1, tree1Copy);

        // same object -> returns true
        assertEquals(TREE1, TREE1);
        assertEquals(TREE2, TREE2);

        // diff object -> returns false
        assertNotEquals(TREE1, TREE2);

        // null -> returns false
        assertNotEquals(TREE1, null);
        assertNotEquals(TREE2, null);

        // same hash, all other attributes different -> return true
        Tree editedTree1SameHash = Tree.of(TREE1.getHash(), "Bob", "TheBuilder");
        Tree editedTree2SameHash = Tree.of(TREE2.getHash(),
                List.of("BOB.png", "CAT.java"),
                List.of("DOG", "Hello?"));
        assertEquals(TREE1, editedTree1SameHash);
        assertEquals(TREE2, editedTree2SameHash);

        // different hash, all other attributes same -> return false
        Tree editedTree1DiffHash = Tree.of("Hello?", "hello.world", "world.hello");
        Tree editedTree2DiffHash = Tree.of("World?",
                List.of("Hello.png", "Hello World.java"),
                List.of("world_hello", "world_hello?"));
        assertNotEquals(TREE1, editedTree1DiffHash);
        assertNotEquals(TREE2, editedTree2DiffHash);

        // null commits -> returns false
        assertNotEquals(TREE1, Tree.emptyTree());
        assertNotEquals(TREE2, Tree.emptyTree());

        // null commits and null commits -> return true
        assertEquals(Tree.emptyTree(), Tree.emptyTree());
    }

    @Test
    public void constructor() {
        // Standard inputs
        assertDoesNotThrow(() -> Tree.of("Tests", "TESTS", "TESTS"));
        assertDoesNotThrow(() -> Tree.of("Tests", List.of("Test"), List.of("Test")));

        // Different list sizes
        assertThrows(IllegalArgumentException.class, () -> Tree.of("testing", List.of("Test"), List.of()));

        //Nulls
        assertThrows(NullPointerException.class, () -> Tree.of("Tests", List.of("Test"), null));
        assertThrows(NullPointerException.class, () -> Tree.of("Tests", null, List.of("Test")));
        assertThrows(NullPointerException.class, () -> Tree.of(null, List.of("Test"), List.of("Test")));
    }

    @Test
    public void regenerateBlobs_vcPathPresent_regeneratedCorrectly() {
        Path vcFile = tempPath.resolve("HelloWorld.txt");
        assertDoesNotThrow(() -> FileUtil.createIfMissing(vcFile));
        assertDoesNotThrow(() -> FileUtil.writeToFile(vcFile, "TESTING!"));

        Path targetFile = tempPath.resolve("WorldHello.txt");
        Tree tree = Tree.of("Testing", targetFile.toString(), vcFile.toString());
        assertDoesNotThrow(tree::regenerateBlobs);
        assertTrue(FileUtil.isFileExists(targetFile));

        byte[] expectedFile = assertDoesNotThrow(() -> Files.readAllBytes(vcFile));
        byte[] actualFile = assertDoesNotThrow(() -> Files.readAllBytes(targetFile));
        assertArrayEquals(expectedFile, actualFile);
    }

    @Test
    public void regenerateBlobs_targetPathPresent_overwriteTarget() {
        Path vcFile = tempPath.resolve("HelloWorld.txt");
        assertDoesNotThrow(() -> FileUtil.createIfMissing(vcFile));
        assertDoesNotThrow(() -> FileUtil.writeToFile(vcFile, "TESTING!"));

        Path targetFile = tempPath.resolve("WorldHello.txt");
        assertDoesNotThrow(() -> FileUtil.createIfMissing(targetFile));
        assertDoesNotThrow(() -> FileUtil.writeToFile(targetFile, "SomethingElse"));

        Tree tree = Tree.of("Testing", targetFile.toString(), vcFile.toString());
        assertDoesNotThrow(tree::regenerateBlobs);
        assertTrue(FileUtil.isFileExists(targetFile));

        byte[] expectedFile = assertDoesNotThrow(() -> Files.readAllBytes(vcFile));
        byte[] actualFile = assertDoesNotThrow(() -> Files.readAllBytes(targetFile));
        assertArrayEquals(expectedFile, actualFile);
    }

    @Test
    public void regenerateBlobs_vcPathAbsent_ioException() {
        Path vcFile = tempPath.resolve("HelloWorld.txt");
        Path targetFile = tempPath.resolve("WorldHello.txt");

        Tree tree = Tree.of("Testing", targetFile.toString(), vcFile.toString());
        assertThrows(IOException.class, tree::regenerateBlobs);
    }
}
