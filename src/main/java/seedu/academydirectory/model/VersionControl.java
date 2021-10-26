package seedu.academydirectory.model;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import seedu.academydirectory.commons.util.FileUtil;
import seedu.academydirectory.storage.VersionControlReader;
import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Label;
import seedu.academydirectory.versioncontrol.objects.StageArea;
import seedu.academydirectory.versioncontrol.objects.Tree;
import seedu.academydirectory.versioncontrol.utils.HashGenerator;
import seedu.academydirectory.versioncontrol.utils.HashMethod;

public class VersionControl {
    public static final String HEAD_LABEL_STRING = "HEAD";
    public static final String OLD_LABEL_STRING = "OLD";
    public static final String CURRENT_LABEL_STRING = "CURRENT";

    private Commit headCommit = Commit.NULL;

    private final Path storagePath;
    private final VersionControlReader versionControlReader;
    private final StageArea stageArea = new StageArea(headCommit);

    /**
     * Models the internal version control system in memory
     * @param hashMethod Which hash function to use
     * @param vcPath Path to save version control related files
     * @param storagePath Path to file to be version controlled
     */
    public VersionControl(HashMethod hashMethod, Path vcPath, Path storagePath) {
        this.versionControlReader = new VersionControlReader(hashMethod, vcPath);
        HashGenerator generator = new HashGenerator(hashMethod);

        this.storagePath = storagePath;
        // Make versionControlled path if not exist
        try {
            Path headPath = vcPath.resolve(Paths.get(HEAD_LABEL_STRING));
            FileUtil.createParentDirsOfFile(headPath);
            if (FileUtil.isFileExists(headPath)) {
                this.headCommit = fetchCommitByLabel(HEAD_LABEL_STRING);
            } else {
                // Create initial commit
                this.headCommit = Commit.NULL;
                this.commit("Initial Commit");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Commits current state to memory to be version controlled
     * @param message Commit message to be used
     */
    public void commit(String message) {
        requireNonNull(message);
        Commit parentCommit = this.headCommit;

        Tree blobTree = versionControlReader.createNewTree(storagePath);
        this.headCommit = versionControlReader.createNewCommit(message, () -> blobTree, () -> parentCommit);
        Label headLabel = versionControlReader.createNewLabel(HEAD_LABEL_STRING, headCommit);

        // Handling branching
        Label oldLabel = versionControlReader.fetchLabelByName(OLD_LABEL_STRING);
        Label currLabel = versionControlReader.fetchLabelByName(CURRENT_LABEL_STRING);
        Commit currCommit = currLabel.getCommitSupplier().get();
        if (!currCommit.equals(Commit.NULL) && !currCommit.equals(parentCommit)) {
            // Branching occurs => oldLabel points to parentCommit and currLabel points to new commit (HEAD commit)
            oldLabel = versionControlReader.createNewLabel(OLD_LABEL_STRING, currCommit);
        }

        currLabel = versionControlReader.createNewLabel(CURRENT_LABEL_STRING, this.headCommit);

        stageArea.resetStage();
        stageArea.stage(headLabel);
        stageArea.stage(oldLabel);
        stageArea.stage(currLabel);
        stageArea.stage(this.headCommit);
        stageArea.stage(blobTree);
    }

    /**
     * Reverts state according to the given hash
     * @param fiveCharHash Hash of the commit to be reverted to
     * @return Commit whose hash is equal to the hash given
     * @throws IOException Unable to restore state correctly
     */
    public Commit revert(String fiveCharHash) throws IOException {
        Commit relevantCommit = versionControlReader.fetchCommitByHash(fiveCharHash);
        if (relevantCommit.equals(Commit.NULL)) { // Error in commit fetching => return null
            return Commit.NULL;
        } else if (relevantCommit.equals(headCommit)) { // Disallowed operation => return null
            return Commit.NULL;
        }

        // Regenerate files
        Tree relevantTree = relevantCommit.getTreeSupplier().get();
        regenerateBlobs(relevantTree);

        this.headCommit = relevantCommit;
        Label headLabel = versionControlReader.createNewLabel(HEAD_LABEL_STRING, headCommit);

        stageArea.resetStage();
        stageArea.stage(headLabel);
        return relevantCommit;
    }

    public StageArea getStageArea() {
        return stageArea;
    }

    public Commit getHeadCommit() {
        return headCommit;
    }

    public Commit fetchCommitByLabel(String labelName) {
        return versionControlReader.fetchLabelByName(labelName).getCommitSupplier().get();
    }

    private void regenerateBlobs(Tree tree) throws IOException {
        HashMap<String, String> hashMap = tree.getHashMap();
        for (String vcFilename : hashMap.keySet()) {
            String actualFilename = hashMap.get(vcFilename);
            Path actualFilepath = Paths.get(actualFilename);
            Path vcFilepath = Paths.get(vcFilename);
            Files.copy(vcFilepath, actualFilepath, REPLACE_EXISTING);
        }
    }
}
