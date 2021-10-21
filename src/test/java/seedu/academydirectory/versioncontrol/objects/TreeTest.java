package seedu.academydirectory.versioncontrol.objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.academydirectory.testutil.TypicalTrees.TREE1;
import static seedu.academydirectory.testutil.TypicalTrees.TREE2;

import java.util.List;

import org.junit.jupiter.api.Test;

public class TreeTest {
    @Test
    public void equals() {
        // same values -> returns true
        Tree tree1Copy = new Tree(TREE1.getHash(), "hello.world", "world.hello");
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
        Tree editedTree1SameHash = new Tree(TREE1.getHash(), "Bob", "TheBuilder");
        Tree editedTree2SameHash = new Tree(TREE2.getHash(),
                List.of("BOB.png", "CAT.java"),
                List.of("DOG", "Hello?"));
        assertEquals(TREE1, editedTree1SameHash);
        assertEquals(TREE2, editedTree2SameHash);

        // different hash, all other attributes same -> return false
        Tree editedTree1DiffHash = new Tree("Hello?", "hello.world", "world.hello");
        Tree editedTree2DiffHash = new Tree("World?",
                List.of("Hello.png", "Hello World.java"),
                List.of("world_hello", "world_hello?"));
        assertNotEquals(TREE1, editedTree1DiffHash);
        assertNotEquals(TREE2, editedTree2DiffHash);

        // null commits -> returns false
        assertNotEquals(TREE1, Tree.NULL);
        assertNotEquals(TREE2, Tree.NULL);

        // null commits and null commits -> return true
        assertEquals(Tree.NULL, Tree.NULL);
    }

    @Test
    public void constructor() {
        // Different list sizes
        assertThrows(IllegalArgumentException.class, () -> new Tree("testing", List.of("Test"), List.of()));
    }
}
