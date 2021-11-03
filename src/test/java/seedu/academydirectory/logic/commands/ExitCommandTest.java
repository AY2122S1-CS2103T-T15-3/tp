package seedu.academydirectory.logic.commands;

import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.academydirectory.logic.commands.ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.VersionedModelManager;

public class ExitCommandTest {
    private VersionedModel model = new VersionedModelManager();
    private VersionedModel expectedModel = new VersionedModelManager();

    @Test
    public void execute_exit_success() {
        CommandResult expectedCommandResult = new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
        assertCommandSuccess(new ExitCommand(), model, expectedCommandResult, expectedModel);
    }
}
