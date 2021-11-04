package seedu.academydirectory.versioncontrol.utils;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class HashGeneratorTest {
    private static final HashMap<HashMethod, String> expectedHash = new HashMap<>();
    private static final Path RELEVANT_DIR = Paths.get("src", "test",
            "data", "VersionControlTest", "StorageManagerTest");
    private static final String filename = "CommitStorageManagerTest";

    @TempDir
    public Path tempPath;

    @Test
    public void hashGenerator_correctHash() {
        // Guard clause since this integration test does not work in Windows CI due to lack of write permissions
        if (!tempPath.toFile().setWritable(true)) {
            return;
        }
        Path filepath = RELEVANT_DIR.resolve(Paths.get(filename));
        Path targetPath = tempPath.resolve(Paths.get(filename));
        assertTrue(() -> filepath.toFile().exists());
        assertDoesNotThrow(() -> Files.copy(filepath, targetPath, REPLACE_EXISTING));

        expectedHash.put(HashMethod.MD5, "20afe0516ba98868d4f0787cd13bc59d");
        expectedHash.put(HashMethod.SHA1, "baaa1d6d49f3e50a7b1a02419f5c1889cd15b5aa");
        expectedHash.put(HashMethod.SHA256, "c8c1f5e07b8792d4190e0fca97efb0e3160bb840af888aa9a080c68f3f549669");

        // MD5
        HashMethod hashMethod = HashMethod.MD5;
        HashGenerator generator = new HashGenerator(hashMethod);
        HashGenerator finalGenerator1 = generator;
        String actualHash = assertDoesNotThrow(() -> finalGenerator1.generateHashFromFile(filepath));
        assertEquals(expectedHash.get(hashMethod), actualHash);

        // SHA1
        hashMethod = HashMethod.SHA1;
        generator = new HashGenerator(hashMethod);
        HashGenerator finalGenerator2 = generator;
        actualHash = assertDoesNotThrow(() -> finalGenerator2.generateHashFromFile(filepath));
        assertEquals(expectedHash.get(hashMethod), actualHash);

        // SHA256
        hashMethod = HashMethod.SHA256;
        generator = new HashGenerator(hashMethod);
        HashGenerator finalGenerator3 = generator;
        actualHash = assertDoesNotThrow(() -> finalGenerator3.generateHashFromFile(filepath));
        assertEquals(expectedHash.get(hashMethod), actualHash);
    }
}
