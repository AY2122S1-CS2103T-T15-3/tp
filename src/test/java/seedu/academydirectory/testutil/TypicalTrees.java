package seedu.academydirectory.testutil;

import java.util.ArrayList;
import java.util.List;

import seedu.academydirectory.versioncontrol.objects.Tree;

/**
 * A utility class containing a list of {@code Tree} objects to be used in tests.
 */
public class TypicalTrees {
    public static final Tree TREE1 = new Tree("1234ThisIsFun!", "hello.world", "world.hello");
    public static final Tree TREE2 = new Tree("123456789ThisIsFun!",
            List.of("Hello.png", "Hello World.java"),
            List.of("world_hello", "world_hello?"));

    public static List<Tree> getTypicalTrees() {
        return new ArrayList<>(List.of(TREE1, TREE2));
    }
}
