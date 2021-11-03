package seedu.academydirectory.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.academydirectory.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.VersionedModelManager;

public class HelpCommandTest {
    private final VersionedModel model = new VersionedModelManager();

    @Test
    public void valid_help_commands() {
        HelpCommand helpCommand1 = new HelpCommand();
        assertEquals(helpCommand1, new HelpCommand());
        assertNotEquals(helpCommand1, new HelpCommand("add", AddCommand.HELP_MESSAGE));
        assertNotEquals(helpCommand1, "Life");
        HelpCommand helpCommand2 = new HelpCommand("grade", GradeCommand.HELP_MESSAGE);
        assertEquals(helpCommand2, new HelpCommand("grade", GradeCommand.HELP_MESSAGE));
        assertNotEquals(helpCommand2, helpCommand1);
        HelpCommand helpCommand3 = new HelpCommand("please send help right now", "random help message");
        assertNotEquals(helpCommand3, helpCommand2);
        assertNotEquals(helpCommand3, 3);
    }

    @Test
    public void execute_valid_help() {
        HelpCommand helpCommand1 = new HelpCommand();
        CommandResult commandResult1 = new CommandResult(HelpCommand.MESSAGE_HELP_SUCCESS_GENERAL,
                HelpCommand.DEFAULT_MESSAGE);
        assertCommandSuccess(helpCommand1, model, commandResult1, model);

        HelpCommand helpCommand2 = new HelpCommand("grade", GradeCommand.HELP_MESSAGE);
        CommandResult commandResult2 = new CommandResult(String.format(HelpCommand.MESSAGE_HELP_SUCCESS_SPECIFIC,
                "grade"), GradeCommand.HELP_MESSAGE);
        assertCommandSuccess(helpCommand2, model, commandResult2, model);

        HelpCommand helpCommand3 = new HelpCommand("exit", ExitCommand.HELP_MESSAGE);
        CommandResult commandResult3 = new CommandResult(String.format(HelpCommand.MESSAGE_HELP_SUCCESS_SPECIFIC,
                "exit"), ExitCommand.HELP_MESSAGE);
        assertCommandSuccess(helpCommand3, model, commandResult3, model);
    }
}
