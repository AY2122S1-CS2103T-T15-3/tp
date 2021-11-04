package seedu.academydirectory.model;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Label;
import seedu.academydirectory.versioncontrol.objects.Tree;
import seedu.academydirectory.versioncontrol.reader.VersionControlGeneralReader;
import seedu.academydirectory.versioncontrol.utils.HashComputer;
import seedu.academydirectory.versioncontrol.utils.HashMethod;

public class VersionControlReader {
    private final VersionControlGeneralReader versionControlGeneralReader;
    private final HashComputer hashComputer;
    private final Path vcPath;

    /**
     * Constructs a VersionControlReader which can load version control files in disk for use programmatically
     * @param hashMethod Hash function to be used to construct version control objects
     * @param vcPath Path to load version control files from
     */
    public VersionControlReader(HashMethod hashMethod, Path vcPath) {
        this.vcPath = vcPath;

        this.versionControlGeneralReader = new VersionControlGeneralReader(vcPath);
        this.hashComputer = new HashComputer(hashMethod);
    }

    /**
     * Constructs a new Commit object to be saved to disk.
     * @param message Commit message for a human-readable format of the commit
     * @param treeSupplier Supplier to the tree object that this Commit points to
     * @param parentCommitSupplier Supplier to the parent Commit that this Commit points to
     * @return a Commit object which represents the change. Returns a null commit if unable to write
     */
    public Commit createNewCommit(String message, Supplier<Tree> treeSupplier, Supplier<Commit> parentCommitSupplier) {
        requireNonNull(message);
        requireNonNull(treeSupplier);
        requireNonNull(parentCommitSupplier);

        String commitFileName = "temp";
        String author = System.getProperty("user.name");
        Date date = new Date();
        Commit temp = Commit.of(commitFileName, author, date, message, parentCommitSupplier, treeSupplier);

        String commitHash = hashComputer.generateHashForObject(temp);
        if (commitHash == null) {
            return Commit.emptyCommit();
        }
        return Commit.of(commitHash, author, date, message, parentCommitSupplier, treeSupplier);
    }

    /**
     * Constructs a Label object to be saved to disk.
     * @param name Label name
     * @param commit Commit to be labelled
     * @return a Label object which represents a label of the given commit. Returns a null label if unable to write
     */
    public Label createNewLabel(String name, Commit commit) {
        requireNonNull(name);
        requireNonNull(commit);

        String labelFileName = "temp";
        Label temp = Label.of(labelFileName, name, () -> commit);

        String labelHash = hashComputer.generateHashForObject(temp);
        if (labelHash == null) {
            return Label.emptyLabel();
        }
        return Label.of(labelHash, name, () -> commit);
    }

    /**
     * Constructs a Tree object to be saved to disk
     * @param blobPaths Path to blobs to be duplicated
     * @return a Tree object which represents mapping between actual filename and version-controlled filename
     */
    public Tree createNewTree(List<Path> blobPaths) {
        // Make a blob snapshot
        ArrayList<Path> blobTargetPaths = new ArrayList<>();
        for (Path blobPath: blobPaths) {
            String blobHash = hashComputer.generateHashFromFile(blobPath);
            if (blobHash == null) {
                return Tree.emptyTree();
            }
            Path blobTargetPath = this.vcPath.resolve(Path.of(blobHash));

            try {
                Files.copy(blobPath, blobTargetPath, REPLACE_EXISTING);
            } catch (IOException e) {
                return Tree.emptyTree();
            }
            blobTargetPaths.add(blobTargetPath);
        }

        // Write a temporary tree to disk
        String treeFileName = "temp";
        Tree temp = Tree.of(treeFileName,
                blobPaths.stream().map(String::valueOf).collect(Collectors.toList()),
                blobTargetPaths.stream().map(String::valueOf).collect(Collectors.toList()));

        String treeHash = hashComputer.generateHashForObject(temp);
        if (treeHash == null) {
            return Tree.emptyTree();
        }
        return Tree.of(treeHash,
                blobPaths.stream().map(String::valueOf).collect(Collectors.toList()),
                blobTargetPaths.stream().map(String::valueOf).collect(Collectors.toList()));
    }

    public Tree createNewTree(Path path) {
        return createNewTree(List.of(path));
    }

    /**
     * Constructs a commit based on hash. Will find the commit object with the same hash in disk
     * @param hash Hash of the commit saved in disk
     * @return Commit object of the given hash
     */
    public Commit fetchCommitByHash(String hash) {
        return versionControlGeneralReader.readCommit(hash);
    }

    /**
     * Find a label on disk based on name.
     * @param labelName Hash of the commit saved in disk
     * @return Label object of the given hash
     */
    public Label fetchLabelByName(String labelName) {
        return versionControlGeneralReader.readLabel(labelName);
    }

    /**
     * Constructs a tree based on hash. Will find the tree object with the same hash in disk
     * @param hash Hash of the tree saved in disk
     * @return Commit object of the given hash
     */
    public Tree fetchTreeByHash(String hash) {
        return versionControlGeneralReader.readTree(hash);
    }
}

