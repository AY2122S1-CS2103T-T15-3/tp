package seedu.academydirectory.model;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.academydirectory.commons.util.FileUtil;
import seedu.academydirectory.storage.VersionControlReader;
import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Label;
import seedu.academydirectory.versioncontrol.objects.StageArea;
import seedu.academydirectory.versioncontrol.objects.Tree;
import seedu.academydirectory.versioncontrol.utils.HashMethod;

public class VersionControl implements Version {
    public static final String HEAD_LABEL_STRING = "HEAD";
    public static final String OLD_LABEL_STRING = "OLD";
    public static final String CURRENT_LABEL_STRING = "CURRENT";

    private Commit headCommit = Commit.emptyCommit();

    private final Path storagePath;
    private final VersionControlReader versionControlReader;
    private final StageArea stageArea = new StageArea();

    /**
     * Models the internal version control system in memory
     * @param hashMethod Which hash function to use
     * @param vcPath Path to save version control related files
     * @param storagePath Path to file to be version controlled
     */
    public VersionControl(HashMethod hashMethod, Path vcPath, Path storagePath) {
        this.versionControlReader = new VersionControlReader(hashMethod, vcPath);

        this.storagePath = storagePath;
        // Make versionControlled path if not exist
        try {
            Path headPath = vcPath.resolve(Paths.get(HEAD_LABEL_STRING));
            FileUtil.createParentDirsOfFile(headPath);
            if (FileUtil.isFileExists(headPath)) {
                this.headCommit = this.fetchCommitByLabel(HEAD_LABEL_STRING);
            } else {
                // Create initial commit
                this.headCommit = Commit.emptyCommit();
                this.commit("Initial Commit");
            }
        } catch (IOException e) {
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

        // Fetching current memory state
        Tree blobTree = versionControlReader.createNewTree(storagePath);
        this.headCommit = versionControlReader.createNewCommit(message, () -> blobTree, () -> parentCommit);
        Label headLabel = versionControlReader.createNewLabel(HEAD_LABEL_STRING, headCommit);
        Label oldLabel = versionControlReader.fetchLabelByName(OLD_LABEL_STRING);
        Label currLabel = versionControlReader.fetchLabelByName(CURRENT_LABEL_STRING);

        oldLabel = handleCommitBranch(oldLabel, currLabel);
        currLabel = versionControlReader.createNewLabel(CURRENT_LABEL_STRING, this.headCommit);

        stageArea.resetStage();
        stageArea.stage(headLabel);
        stageArea.stage(oldLabel);
        stageArea.stage(currLabel);
        stageArea.stage(this.headCommit);
        stageArea.stage(blobTree);
    }

    private Label handleCommitBranch(Label oldLabel, Label currLabel) {
        Commit currCommit = currLabel.getCommitSupplier().get();
        if (!currCommit.isEmpty() && !currCommit.equals(this.headCommit)) {
            // Branching occurs => label currCommit as "old"
            return versionControlReader.createNewLabel(OLD_LABEL_STRING, currCommit);
        }
        return oldLabel;
    }

    /**
     * Reverts state according to the given hash
     * @param fiveCharHash Hash of the commit to be reverted to
     * @return Commit whose hash is equal to the hash given
     * @throws IOException Unable to restore state correctly
     */
    public Commit revert(String fiveCharHash) throws IOException {
        Commit relevantCommit = versionControlReader.fetchCommitByHash(fiveCharHash);
        if (relevantCommit.equals(Commit.emptyCommit())) { // Error in commit fetching => return null
            return Commit.emptyCommit();
        } else if (relevantCommit.equals(headCommit)) { // Disallowed operation => return null
            return Commit.emptyCommit();
        }

        // Regenerate files
        Tree relevantTree = relevantCommit.getTreeSupplier().get();
        relevantTree.regenerateBlobs();

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
}
