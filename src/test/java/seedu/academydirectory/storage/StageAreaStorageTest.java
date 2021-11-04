package seedu.academydirectory.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.academydirectory.commons.util.FileUtil;
import seedu.academydirectory.model.StageArea;
import seedu.academydirectory.testutil.TypicalCommits;
import seedu.academydirectory.testutil.TypicalTrees;
import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Label;
import seedu.academydirectory.versioncontrol.objects.Tree;
import seedu.academydirectory.versioncontrol.objects.VcObject;

public class StageAreaStorageTest {

    @TempDir
    public Path tempPath;

    @Test
    public void saveStageArea_commits() {
        StageAreaStorage stageAreaStorage = new StageAreaStorage(tempPath);
        testSaveStageArea(stageAreaStorage, TypicalCommits.getTypicalCommits());
        testSaveStageArea(stageAreaStorage, TypicalCommits.getTypicalCommitList());
        testSaveStageArea(stageAreaStorage, TypicalCommits.getTypicalCommitTree());

        // Negative Testing
        testSaveStageArea_negativeTesting(stageAreaStorage, null);
        testSaveStageArea_negativeTesting(stageAreaStorage, Commit.emptyCommit());
    }

    @Test
    public void saveStageArea_trees() {
        StageAreaStorage stageAreaStorage = new StageAreaStorage(tempPath);
        testSaveStageArea(stageAreaStorage, List.of(TypicalTrees.TREE1));

        // Negative Testing
        testSaveStageArea_negativeTesting(stageAreaStorage, null);
        testSaveStageArea_negativeTesting(stageAreaStorage, Tree.emptyTree());
    }

    @Test
    public void saveStageArea_labels() {
        StageAreaStorage stageAreaStorage = new StageAreaStorage(tempPath);
        HashMap<Label, String> labelNameMapping = new HashMap<>();

        TypicalCommits.getTypicalLabels().forEach(label -> labelNameMapping.put(label, label.getName()));
        testSaveStageAreaLabel(stageAreaStorage, labelNameMapping);

        TypicalCommits.getTypicalLabelList().forEach(label -> labelNameMapping.put(label, label.getName()));
        testSaveStageAreaLabel(stageAreaStorage, labelNameMapping);

        TypicalCommits.getTypicalLabelTree().forEach(label -> labelNameMapping.put(label, label.getName()));
        testSaveStageAreaLabel(stageAreaStorage, labelNameMapping);

        // Negative Testing
        testSaveStageArea_negativeTesting(stageAreaStorage, null);
        testSaveStageArea_negativeTesting(stageAreaStorage, Label.emptyLabel());
    }

    private void testSaveStageArea(StageAreaStorage stageAreaStorage, List<? extends VcObject> vcObjectsList) {
        StageArea stageArea = new StageArea();
        vcObjectsList.forEach(stageArea::stage);
        assertDoesNotThrow(() -> stageAreaStorage.saveStageArea(stageArea));
        stageArea.getVcObjectList().stream().map(VcObject::getHash)
                .forEach(hash -> assertTrue(FileUtil.isFileExists(tempPath.resolve(hash))));
    }

    private void testSaveStageArea_negativeTesting(StageAreaStorage stageAreaStorage, VcObject vcObject) {
        StageArea stageArea = new StageArea();
        stageArea.stage(vcObject);
        assertThrows(IOException.class, () -> stageAreaStorage.saveStageArea(stageArea));
    }

    private void testSaveStageAreaLabel(StageAreaStorage stageAreaStorage, HashMap<Label, String> labelNameMapping) {
        StageArea stageArea = new StageArea();
        labelNameMapping.keySet().forEach(stageArea::stage);
        assertDoesNotThrow(() -> stageAreaStorage.saveStageArea(stageArea));
        stageArea.getVcObjectList().stream().filter(x -> x instanceof Label)
                .map(x -> (Label) x).map(labelNameMapping::get)
                .forEach(name -> assertTrue(FileUtil.isFileExists(tempPath.resolve(name))));
    }
}
