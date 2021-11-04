package seedu.academydirectory.model;

import java.util.ArrayList;
import java.util.List;

import seedu.academydirectory.versioncontrol.objects.VcObject;

public class StageArea {
    private List<VcObject> stageArea;

    /**
     * Construct a StageArea for version control purposes. Objects staged onto stageArea can be saved to disk
     * @param vcObjects Version Control objects to be saved to disk
     */
    public StageArea(VcObject... vcObjects) {
        resetStage();
        stageArea.addAll(List.of(vcObjects));
    }

    public void stage(VcObject vcObject) {
        stageArea.add(vcObject);
    }

    public void resetStage() {
        stageArea = new ArrayList<>();
    }

    public List<VcObject> getVcObjectList() {
        return stageArea;
    }

    public boolean isEmpty() {
        return stageArea.isEmpty();
    }
}
