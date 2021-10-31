package seedu.academydirectory.storage;

import java.io.IOException;
import java.nio.file.Path;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Label;
import seedu.academydirectory.versioncontrol.objects.StageArea;
import seedu.academydirectory.versioncontrol.objects.Tree;
import seedu.academydirectory.versioncontrol.objects.VcObject;
import seedu.academydirectory.versioncontrol.storage.CommitStorageManager;
import seedu.academydirectory.versioncontrol.storage.LabelStorageManager;
import seedu.academydirectory.versioncontrol.storage.TreeStorageManager;

public class StageAreaStorage {
    private final TreeStorageManager treeStorageManager;
    private final CommitStorageManager commitStorageManager;
    private final LabelStorageManager labelStorageManager;

    /**
     * Creates a storage for {@code StageArea}. This class defines how StageArea should be stored in disk e.g.
     * in which folder, in what format etc.
     * @param vcPath Path to save VcObject to
     */
    public StageAreaStorage(Path vcPath) {
        this.treeStorageManager = new TreeStorageManager(vcPath);
        this.commitStorageManager = new CommitStorageManager(vcPath, treeStorageManager);
        this.labelStorageManager = new LabelStorageManager(vcPath, commitStorageManager);
    }

    /**
     * Writes a given StageArea to disk
     * @param stageArea The StageArea to be written to disk
     * @throws IOException Unable to write to disk
     */
    public void saveStageArea(StageArea stageArea) throws IOException {
        boolean nothingWrong = true;
        if (stageArea.getVcObjectList().size() == 0) {
            return;
        }
        for (VcObject vcObject : stageArea.getVcObjectList()) {
            try {
                write(vcObject);
            } catch (IOException e) {
                e.printStackTrace();
                nothingWrong = false;
            }
        }

        stageArea.resetStage();
        if (!nothingWrong) {
            throw new IOException("Unable to save one or more VcObject");
        }
    }

    private void write(VcObject vcObject) throws IOException {
        if (vcObject instanceof Commit && !((Commit) vcObject).equals(Commit.NULL)) {
            Commit commit = (Commit) vcObject;
            commitStorageManager.write(commit.getHash(), commit);
        } else if (vcObject instanceof Tree && !((Tree) vcObject).equals(Tree.NULL)) {
            Tree tree = (Tree) vcObject;
            treeStorageManager.write(tree.getHash(), tree);
        } else if (vcObject instanceof Label && !((Label) vcObject).equals(Label.NULL)) {
            Label label = (Label) vcObject;
            labelStorageManager.write(label.getName(), label);
        } else {
            throw new IOException("Unable to write given VcObject");
        }
    }
}
