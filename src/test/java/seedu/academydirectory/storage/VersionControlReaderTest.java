package seedu.academydirectory.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT1;
import static seedu.academydirectory.testutil.TypicalTrees.TREE1;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.academydirectory.model.VersionControlReader;
import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Label;
import seedu.academydirectory.versioncontrol.objects.Tree;
import seedu.academydirectory.versioncontrol.utils.HashGenerator;
import seedu.academydirectory.versioncontrol.utils.HashMethod;

public class VersionControlReaderTest {
    @TempDir
    public static Path testingDir;

    private static final Path DATA_DIR = Paths.get("src", "test",
            "data", "VersionControlTest");
    private static final Path FETCH_OBJECT_DIR = DATA_DIR.resolve("FetchObjectTest");
    private static final HashMethod hashMethod = HashMethod.SHA1;
    private static final HashGenerator hashGenerator = new HashGenerator(hashMethod);

    @Test
    public void createNewCommit() {
        VersionControlReader versionControlReader = new VersionControlReader(hashMethod, testingDir);

        // Null Tree and Null Parent Commit
        String message = "Initial Commit";
        Supplier<Tree> nullTreeSupplier = Tree::emptyTree;
        Supplier<Commit> nullCommitSupplier = Commit::emptyCommit;
        Commit currentCommit = assertDoesNotThrow(() -> versionControlReader.createNewCommit(message,
                nullTreeSupplier,
                nullCommitSupplier));

        assertEquals(System.getProperty("user.name"), currentCommit.getAuthor());
        assertEquals(currentCommit.getDate(), currentCommit.getDate());
        assertEquals(message, currentCommit.getMessage());
        assertEquals(nullCommitSupplier, currentCommit.getParentSupplier());
        assertEquals(nullTreeSupplier, currentCommit.getTreeSupplier());

        // Null Tree but Non-Null parent Commit
        currentCommit = assertDoesNotThrow(() -> versionControlReader.createNewCommit(message,
                nullTreeSupplier, () -> COMMIT1));

        assertEquals(System.getProperty("user.name"), currentCommit.getAuthor());
        assertEquals(currentCommit.getDate(), currentCommit.getDate());
        assertEquals(message, currentCommit.getMessage());
        assertEquals(COMMIT1, currentCommit.getParentSupplier().get());
        assertEquals(nullTreeSupplier, currentCommit.getTreeSupplier());

        // Non-Null Tree but Null parent Commit
        currentCommit = assertDoesNotThrow(() -> versionControlReader.createNewCommit(message, () -> TREE1,
                nullCommitSupplier));

        assertEquals(System.getProperty("user.name"), currentCommit.getAuthor());
        assertEquals(currentCommit.getDate(), currentCommit.getDate());
        assertEquals(message, currentCommit.getMessage());
        assertEquals(nullCommitSupplier, currentCommit.getParentSupplier());
        assertEquals(TREE1, currentCommit.getTreeSupplier().get());

        // Non-Null Tree and Non-Null Commit
        currentCommit = assertDoesNotThrow(() -> versionControlReader.createNewCommit(
                message, () -> TREE1, () -> COMMIT1));

        assertEquals(System.getProperty("user.name"), currentCommit.getAuthor());
        assertEquals(currentCommit.getDate(), currentCommit.getDate());
        assertEquals(message, currentCommit.getMessage());
        assertEquals(COMMIT1, currentCommit.getParentSupplier().get());
        assertEquals(TREE1, currentCommit.getTreeSupplier().get());
    }

    @Test
    public void fetchCommitByHash_correctHash_correctCommit() {
        VersionControlReader versionControlReader = new VersionControlReader(hashMethod, FETCH_OBJECT_DIR);

        // Exact Hash used -> Commit fetched successfully
        String commitHash = "1d83638a25901e76c8e3882afca2347f8352cd06";
        Path filepath = FETCH_OBJECT_DIR.resolve(Paths.get(commitHash));
        assertTrue(filepath.toFile().exists()); // Check if file exists first

        Commit actualCommit = versionControlReader.fetchCommitByHash(commitHash);
        assertEquals(COMMIT1, actualCommit);

        // fiveCharHash used -> Commit fetched successfully
        commitHash = commitHash.substring(0, 5);
        actualCommit = versionControlReader.fetchCommitByHash(commitHash);
        assertEquals(COMMIT1, actualCommit);
    }

    @Test
    public void fetchCommitByHash_missingHash_nullCommit() {
        VersionControlReader versionControlReader = new VersionControlReader(hashMethod, FETCH_OBJECT_DIR);

        // given hash not present in disk -> Commit.NULL returned
        String commitHash = "MMISSING";
        Path filepath = FETCH_OBJECT_DIR.resolve(Paths.get(commitHash));
        assertFalse(filepath.toFile().exists()); // Check if file doesn't exist first
        assertTrue(versionControlReader.fetchCommitByHash(commitHash).isEmpty());
    }

    @Test
    public void fetchCommitByHash_corruptedCommit_nullCommit() {
        VersionControlReader versionControlReader = new VersionControlReader(hashMethod, FETCH_OBJECT_DIR);

        // given corrupt commit file -> Commit.NULL returned
        String commitHash = "corruptedCommit";
        Path filepath = FETCH_OBJECT_DIR.resolve(Paths.get(commitHash));
        assertTrue(filepath.toFile().exists()); // Check if file exists first
        assertTrue(versionControlReader.fetchCommitByHash(commitHash).isEmpty());
    }

    @Test
    public void createNewTree_blobsPresent_correctlyDuplicated() {
        VersionControlReader versionControlReader = new VersionControlReader(hashMethod, testingDir);

        // Blobs present -> Correctly Duplicated
        String blobName = "HEAD";
        Path blobPath = FETCH_OBJECT_DIR.resolve(Paths.get(blobName));
        assertTrue(blobPath.toFile().exists()); // Check if blob exists first

        Tree actualTree = versionControlReader.createNewTree(blobPath);
        HashMap<String, String> hashMap = actualTree.getHashMap();
        String[] keySet = hashMap.keySet().toArray(new String[0]);
        for (String vcName : keySet) {
            String realName = hashMap.get(vcName);

            Path actualPath = Paths.get(realName);
            assertTrue(actualPath.toFile().exists());

            Path vcPath = Paths.get(vcName);
            assertTrue(vcPath.toFile().exists());

            String actualHash = assertDoesNotThrow(() ->
                    hashGenerator.generateHashFromFile(actualPath));
            String vcHash = assertDoesNotThrow(() ->
                    hashGenerator.generateHashFromFile(actualPath));
            assertEquals(actualHash, vcHash);
        }
    }

    @Test
    public void createNewTree_blobsAbsent_nullTree() {
        VersionControlReader versionControlReader = new VersionControlReader(hashMethod, testingDir);

        String blobName = "TAIL";
        Path blobPath = FETCH_OBJECT_DIR.resolve(Paths.get(blobName));
        assertFalse(blobPath.toFile().exists()); // Check if blob does not exist

        Tree actualTree = versionControlReader.createNewTree(blobPath);
        assertTrue(actualTree.isEmpty());
    }

    @Test
    public void fetchTreeByHash_correctHash_correctTree() {
        VersionControlReader versionControlReader = new VersionControlReader(hashMethod, FETCH_OBJECT_DIR);

        // Exact Hash used -> Commit fetched successfully
        String treeHash = "9d34f3e9ada5ae7cc5c063b905a5d7893f792497";
        Path filepath = FETCH_OBJECT_DIR.resolve(Paths.get(treeHash));
        assertTrue(filepath.toFile().exists()); // Check if file exists first

        Tree actualTree = versionControlReader.fetchTreeByHash(treeHash);
        assertEquals(TREE1.getHash(), actualTree.getHash());

        // fiveCharHash used -> Commit fetched successfully
        treeHash = treeHash.substring(0, 5);
        actualTree = versionControlReader.fetchTreeByHash(treeHash);
        assertEquals(TREE1.getHash(), actualTree.getHash());
    }

    @Test
    public void fetchTreeByHash_missingHash_nullTree() {
        VersionControlReader versionControlReader = new VersionControlReader(hashMethod, FETCH_OBJECT_DIR);

        String treeHash = "MISSING";
        Path filepath = FETCH_OBJECT_DIR.resolve(Paths.get(treeHash));
        assertFalse(filepath.toFile().exists()); // Check if file doesn't exist first
        assertTrue(versionControlReader.fetchTreeByHash(treeHash).isEmpty());
    }

    @Test
    public void fetchTreeByHash_corruptedTree_nullTree() {
        VersionControlReader versionControlReader = new VersionControlReader(hashMethod, FETCH_OBJECT_DIR);

        // given corrupt commit file -> Tree.NULL returned
        String treeHash = "corruptedCommit";
        Path filepath = FETCH_OBJECT_DIR.resolve(Paths.get(treeHash));
        assertTrue(filepath.toFile().exists()); // Check if file exists first
        assertTrue(versionControlReader.fetchTreeByHash(treeHash).isEmpty());
    }

    @Test
    public void createLabel() {
        VersionControlReader versionControlReader = new VersionControlReader(hashMethod, testingDir);

        // null passed in -> exception thrown
        assertThrows(NullPointerException.class, () -> versionControlReader.createNewLabel("NULL", null));
        assertThrows(NullPointerException.class, () -> versionControlReader.createNewLabel(null, null));

        // Commit1 passed in -> Commit1 labelled
        Label labelledCommit = versionControlReader.createNewLabel("temp", COMMIT1);
        assertEquals(labelledCommit.getCommitSupplier().get(), COMMIT1);

        // Commit.NULL passed in -> Commit.NULL labelled
        Label labelledNullCommit = versionControlReader.createNewLabel("temp", Commit.emptyCommit());
        assertTrue(labelledNullCommit.getCommitSupplier().get().isEmpty());
    }

    @Test
    public void fetchLabelByName_labelPresent_correctCommit() {
        VersionControlReader versionControlReader = new VersionControlReader(hashMethod, FETCH_OBJECT_DIR);

        String labelName = "HEAD";
        Path filepath = FETCH_OBJECT_DIR.resolve(Paths.get(labelName));
        assertTrue(filepath.toFile().exists()); // Check if file exists first

        // Correct Name given -> Label fetched successfully
        Label actualLabel = versionControlReader.fetchLabelByName(labelName);
        assertEquals(COMMIT1, actualLabel.getCommitSupplier().get());
    }

    @Test
    public void fetchLabelByName_corruptedLabel_nullCommit() {
        VersionControlReader versionControlReader = new VersionControlReader(hashMethod, FETCH_OBJECT_DIR);

        String labelName = "corruptedHead";
        Path filepath = FETCH_OBJECT_DIR.resolve(Paths.get(labelName));
        assertTrue(filepath.toFile().exists()); // Check if file exists first

        assertTrue(versionControlReader.fetchLabelByName(labelName).isEmpty());
    }

    @Test
    public void fetchLabelByName_missingLabel_nullCommit() {
        VersionControlReader versionControlReader = new VersionControlReader(hashMethod, FETCH_OBJECT_DIR);

        String labelName = "MISSING";
        Path filepath = FETCH_OBJECT_DIR.resolve(Paths.get(labelName));
        assertFalse(filepath.toFile().exists()); // Check if file doesn't exist first

        assertTrue(versionControlReader.fetchLabelByName(labelName).isEmpty());
    }
}
