package seedu.academydirectory.versioncontrol.writer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.academydirectory.testutil.TypicalTrees.TREE1;
import static seedu.academydirectory.testutil.TypicalTrees.TREE2;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.academydirectory.versioncontrol.objects.Tree;

public class TreeWriterTest {
    @TempDir
    public Path tempPath;
    private TreeWriter treeWriter;

    @Test
    public void getWriteableFormat() {
        treeWriter = new TreeWriter(tempPath);

        // Correct Trees
        assertEquals(List.of("world.hello hello.world"), treeWriter.getWriteableFormat(TREE1));
        assertEquals(List.of("world_hello Hello.png", "world_hello? Hello World.java"),
                treeWriter.getWriteableFormat(TREE2));

        // Negative Test: null
        assertThrows(NullPointerException.class, () -> treeWriter.getWriteableFormat(null));
        assertThrows(IllegalArgumentException.class, () -> treeWriter.getWriteableFormat(Tree.emptyTree()));
    }

    @Test
    public void write() {
        treeWriter = new TreeWriter(tempPath);

        // Correct Trees
        assertDoesNotThrow(() -> treeWriter.write(TREE1.getHash(), TREE1));
        assertDoesNotThrow(() -> treeWriter.write(TREE1.getHash(), TREE2));
        assertDoesNotThrow(() -> treeWriter.write(TREE2.getHash(), TREE2));

        // Negative Test: null
        assertThrows(NullPointerException.class, () -> treeWriter.write(TREE1.getHash(), null));
        assertThrows(NullPointerException.class, () -> treeWriter.write(null, TREE1));
        assertThrows(NullPointerException.class, () -> treeWriter.write(null, Tree.emptyTree()));

        assertThrows(IllegalArgumentException.class, () -> treeWriter.write(TREE1.getHash(), Tree.emptyTree()));
    }
}
