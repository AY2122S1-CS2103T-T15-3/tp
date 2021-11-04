package seedu.academydirectory.model;

import java.io.IOException;

import seedu.academydirectory.versioncontrol.objects.Commit;

/**
 * Wraps all version control related data
 */
public interface Version {
    /**
     * Commits a model data change and tie the change with a given message
     * @param message Message attached to the Commit for a readable explanation
     */
    void commit(String message);

    /**
     * Reverts model data state to a previous state stored in disk and identifiable by its hash
     * @param fiveCharHash The five character hash of the previous state to be reverted to
     * @return {@code Commit} object representing the reverted state
     * @throws IOException if unable to revert underlying data to previous state
     */
    Commit revert(String fiveCharHash) throws IOException;

    StageArea getStageArea();

    Commit getHeadCommit();

    Commit fetchCommitByLabel(String labelName);
}
