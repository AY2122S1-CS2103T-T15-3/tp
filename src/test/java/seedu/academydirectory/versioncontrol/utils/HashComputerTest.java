package seedu.academydirectory.versioncontrol.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT1;
import static seedu.academydirectory.testutil.TypicalTrees.TREE1;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Tree;
import seedu.academydirectory.versioncontrol.writer.VersionControlGeneralWriter;

public class HashComputerTest {
    private static final HashMethod[] supportedMethods = {HashMethod.MD5, HashMethod.SHA1, HashMethod.SHA256};
    private static final String filename = "CommitStorageManagerTest";
    private static final Path filepath = Paths.get("src", "test", "data", "VersionControlTest",
            "StorageManagerTest", filename);

    @TempDir
    public Path tempPath;

    @Test
    public void generateHashFromFile() {
        Arrays.stream(supportedMethods).forEach(method -> {
            HashGenerator hashGenerator = new HashGenerator(method);
            HashComputer hashComputer = new HashComputer(method);
            String expectedHash = assertDoesNotThrow(() -> hashGenerator.generateHashFromFile(filepath));
            String actualHash = assertDoesNotThrow(() -> hashComputer.generateHashFromFile(filepath));
            assertEquals(expectedHash, actualHash);
        });
    }

    @Test
    public void generateHashForObject() {
        VersionControlGeneralWriter versionControlGeneralWriter = new VersionControlGeneralWriter(tempPath);

        Arrays.stream(supportedMethods).forEach(method -> {
            Commit commit = COMMIT1;
            HashGenerator hashGenerator = new HashGenerator(method);
            assertDoesNotThrow(() -> versionControlGeneralWriter.writeCommit(commit));
            String expectedHash = assertDoesNotThrow(() -> hashGenerator.generateHashFromFile(
                    tempPath.resolve(commit.getHash())));

            HashComputer hashComputer = new HashComputer(method);
            String actualHash = assertDoesNotThrow(() -> hashComputer.generateHashForObject(commit));
            assertEquals(expectedHash, actualHash);

            Tree tree = TREE1;
            assertDoesNotThrow(() -> versionControlGeneralWriter.writeTree(tree));
            expectedHash = assertDoesNotThrow(() -> hashGenerator.generateHashFromFile(
                    tempPath.resolve(tree.getHash())));

            actualHash = assertDoesNotThrow(() -> hashComputer.generateHashForObject(tree));
            assertEquals(expectedHash, actualHash);
        });
    }
}
