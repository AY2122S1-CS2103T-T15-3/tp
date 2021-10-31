//package seedu.academydirectory.versioncontrol.utils;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.HashMap;
//
//import org.junit.jupiter.api.Test;
//
//public class HashGeneratorTest {
//    private static final HashMethod[] supportedMethods = {HashMethod.SHA1, HashMethod.SHA256};
//    private static final HashMap<HashMethod, String> expectedHash = new HashMap<>();
//    private static final String filename = "CommitStorageManagerTest";
//
//    @Test
//    public void hashGenerator_correctHash() {
//        expectedHash.put(HashMethod.MD5, "00f8e0d3e2f06918e4b3f2ef2709e2df");
//        expectedHash.put(HashMethod.SHA1, "1ba3aa88d488e76d30086af249a8d17072e04185");
//        expectedHash.put(HashMethod.SHA256, "0c54914698ffaee27e52d66038ced1b69c2cea854c16c906c6764196514217d5");
//
//        Path filepath = Paths.get("src", "test", "data", "VersionControlTest", "ParserTest", filename);
//        assertTrue(() -> filepath.toFile().exists());
//
//        // SHA1
//        HashMethod hashMethod = HashMethod.SHA1;
//        HashGenerator generator = new HashGenerator(hashMethod);
//        HashGenerator finalGenerator = generator;
//        String actualHash = assertDoesNotThrow(() -> finalGenerator.generateHashFromFileV2(filepath));
//        assertEquals(expectedHash.get(hashMethod), actualHash);
//
//        // SHA256
//        hashMethod = HashMethod.SHA256;
//        generator = new HashGenerator(hashMethod);
//        HashGenerator finalGenerator1 = generator;
//        actualHash = assertDoesNotThrow(() -> finalGenerator1.generateHashFromFileV2(filepath));
//        assertEquals(expectedHash.get(hashMethod), actualHash);
//    }
//}
