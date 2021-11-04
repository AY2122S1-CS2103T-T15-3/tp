package seedu.academydirectory.versioncontrol.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Label;
import seedu.academydirectory.versioncontrol.objects.Tree;
import seedu.academydirectory.versioncontrol.objects.VcObject;
import seedu.academydirectory.versioncontrol.writer.VersionControlGeneralWriter;

public class HashComputer {
    private static final String tmpFilename = "temp";

    private final Path tmpPath;
    private final VersionControlGeneralWriter versionControlGeneralWriter;
    private final HashGenerator hashGenerator;

    /**
     * Creates a HashComputer which computes hash of a given VcObject, or a file in disk
     * @param hashMethod Hash function to use
     */
    public HashComputer(HashMethod hashMethod) {
        VersionControlGeneralWriter versionControlGeneralWriter1;
        Path tmpPath1;
        try {
            tmpPath1 = Files.createTempDirectory("ADVC");
            versionControlGeneralWriter1 = new VersionControlGeneralWriter(tmpPath1);
        } catch (IOException e) {
            tmpPath1 = null;
            versionControlGeneralWriter1 = null;
        }

        this.hashGenerator = new HashGenerator(hashMethod);
        this.versionControlGeneralWriter = versionControlGeneralWriter1;
        this.tmpPath = tmpPath1;
    }

    /**
     * Generates the hash of a given VcObject
     * @param vcObject VcObject for which the correct hash is to be computed
     * @return Hash of VcObject if nothing goes wrong, null otherwise.
     */
    public String generateHashForObject(VcObject vcObject) {
        if (tmpPath == null) {
            return null;
        }

        try {
            Path filePath = tmpPath.resolve(tmpFilename);
            write(vcObject);
            String hash = hashGenerator.generateHashFromFile(filePath);
            boolean deletedSuccessfully = filePath.toFile().delete();

            return hash;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Generates the hash of the file located at filePath
     * @param filePath path to the file whose hash is to be computed
     * @return hash of the file if everything works correctly, null otherwise.
     */
    public String generateHashFromFile(Path filePath) {
        if (tmpPath == null) {
            return null;
        }
        try {
            return hashGenerator.generateHashFromFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void write(VcObject vcObject) throws NullPointerException, IOException {
        if (vcObject instanceof Commit && !((Commit) vcObject).isEmpty()) {
            Commit commit = (Commit) vcObject;
            versionControlGeneralWriter.writeCommit(Commit.of(tmpFilename, commit.getAuthor(), commit.getDate(),
                    commit.getMessage(), commit.getParentSupplier(), commit.getTreeSupplier()));
        } else if (vcObject instanceof Tree && !((Tree) vcObject).isEmpty()) {
            Tree tree = (Tree) vcObject;
            versionControlGeneralWriter.writeTree(Tree.of(
                    tmpFilename,
                    tree.getHashMap().keySet().stream().map(x -> tree.getHashMap().get(x))
                            .collect(Collectors.toList()),
                    Arrays.stream(tree.getHashMap().keySet().toArray(String[]::new))
                            .collect(Collectors.toList())));
        } else if (vcObject instanceof Label && !((Label) vcObject).isEmpty()) {
            Label label = (Label) vcObject;
            versionControlGeneralWriter.writeLabel(Label.of(tmpFilename, tmpFilename, label.getCommitSupplier()));
        } else {
            throw new NullPointerException("NULL vcObject encountered");
        }
    }
}
