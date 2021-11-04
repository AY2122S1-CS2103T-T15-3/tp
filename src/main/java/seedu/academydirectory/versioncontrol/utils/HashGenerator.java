package seedu.academydirectory.versioncontrol.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {
    private final int numHex;
    private final MessageDigest digest;

    /**
     * Constructs a HashGenerator which can generate hash for a given file
     * @param method Hash algorithms supported: MD1, SHA1 and SHA256
     */
    public HashGenerator(HashMethod method) {
        int numHex1;
        MessageDigest digest1;
        try {
            MessageDigest digest = MessageDigest.getInstance(method.getValue());
            digest.reset();
            digest1 = digest;
            numHex1 = method.getNumHex();
        } catch (NoSuchAlgorithmException e) {
            // Will not happen since HashMethod only includes supported algorithms
            e.printStackTrace();
            digest1 = null;
            numHex1 = 0;
        }
        numHex = numHex1;
        this.digest = digest1;
    }

    /**
     * Generates a hash for a given file
     * @param path Path to the given file
     * @return String which represents hash of the given file
     * @throws IOException Path not found
     */
    public String generateHashFromFile(Path path) throws IOException {
        digest.reset();
        digest.update(Files.readAllBytes(path));
        byte[] hash = digest.digest();
        return String.format("%0" + numHex + "x", new BigInteger(1, hash));
    }
}
