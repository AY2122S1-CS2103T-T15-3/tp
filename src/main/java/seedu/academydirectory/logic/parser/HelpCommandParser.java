package seedu.academydirectory.logic.parser;

import static seedu.academydirectory.commons.core.Messages.MESSAGE_HELP_NOT_EXIST;

import seedu.academydirectory.logic.commands.AddCommand;
import seedu.academydirectory.logic.commands.AttendanceCommand;
import seedu.academydirectory.logic.commands.ClearCommand;
import seedu.academydirectory.logic.commands.DeleteCommand;
import seedu.academydirectory.logic.commands.EditCommand;
import seedu.academydirectory.logic.commands.ExitCommand;
import seedu.academydirectory.logic.commands.FindCommand;
import seedu.academydirectory.logic.commands.GetCommand;
import seedu.academydirectory.logic.commands.GradeCommand;
import seedu.academydirectory.logic.commands.HelpCommand;
import seedu.academydirectory.logic.commands.ListCommand;
import seedu.academydirectory.logic.parser.exceptions.ParseException;

public class HelpCommandParser implements Parser<HelpCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the HelpCommand
     * and returns an HelpCom object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public HelpCommand parse(String userInput) throws ParseException {
        userInput = userInput.trim();
        if (userInput.isEmpty()) {
            return new HelpCommand();
        }

        switch (userInput) {
        case AddCommand.COMMAND_WORD:
            return new HelpCommand(userInput, AddCommand.HELP_MESSAGE);

        case EditCommand.COMMAND_WORD:
            return new HelpCommand(userInput, EditCommand.HELP_MESSAGE);

        case DeleteCommand.COMMAND_WORD:
            return new HelpCommand(userInput, DeleteCommand.HELP_MESSAGE);

        case ClearCommand.COMMAND_WORD:
            return new HelpCommand(userInput, ClearCommand.HELP_MESSAGE);

        case FindCommand.COMMAND_WORD:
            return new HelpCommand(userInput, FindCommand.HELP_MESSAGE);

        case GetCommand.COMMAND_WORD:
            return new HelpCommand(userInput, GetCommand.HELP_MESSAGE);

        case ListCommand.COMMAND_WORD:
            return new HelpCommand(userInput, ListCommand.HELP_MESSAGE);

        case AttendanceCommand.COMMAND_WORD:
            return new HelpCommand(userInput, AttendanceCommand.HELP_MESSAGE);

        case GradeCommand.COMMAND_WORD:
            return new HelpCommand(userInput, GradeCommand.HELP_MESSAGE);

        case ExitCommand.COMMAND_WORD:
            return new HelpCommand(userInput, ExitCommand.HELP_MESSAGE);

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            throw new ParseException(String.format(MESSAGE_HELP_NOT_EXIST, userInput));
        }
    }
}

