package seedu.academydirectory.storage;

import java.io.IOException;
import java.nio.file.Path;

import seedu.academydirectory.model.StageArea;
import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Label;
import seedu.academydirectory.versioncontrol.objects.Tree;
import seedu.academydirectory.versioncontrol.objects.VcObject;
import seedu.academydirectory.versioncontrol.writer.VersionControlGeneralWriter;

public class StageAreaStorage {
    private final VersionControlGeneralWriter versionControlGeneralWriter;

    /**
     * Creates a storage for {@code StageArea}. This class defines how StageArea should be stored in disk e.g.
     * in which folder, in what format etc.
     * @param vcPath Path to save VcObject to
     */
    public StageAreaStorage(Path vcPath) {
        this.versionControlGeneralWriter = new VersionControlGeneralWriter(vcPath);
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
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
                nothingWrong = false;
            }
        }

        stageArea.resetStage();
        if (!nothingWrong) {
            throw new IOException("Unable to save one or more VcObject");
        }
    }

    private void write(VcObject vcObject) throws NullPointerException, IOException {
        if (vcObject instanceof Commit && !((Commit) vcObject).isEmpty()) {
            Commit commit = (Commit) vcObject;
            versionControlGeneralWriter.writeCommit(commit);
        } else if (vcObject instanceof Tree && !((Tree) vcObject).isEmpty()) {
            Tree tree = (Tree) vcObject;
            versionControlGeneralWriter.writeTree(tree);
        } else if (vcObject instanceof Label && !((Label) vcObject).isEmpty()) {
            Label label = (Label) vcObject;
            versionControlGeneralWriter.writeLabel(label);
        } else {
            throw new NullPointerException("NULL vcObject encountered");
        }
    }
}
