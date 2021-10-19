package seedu.academydirectory.versioncontrol.logic.parsers;

import static seedu.academydirectory.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.academydirectory.logic.parser.exceptions.ParseException;
import seedu.academydirectory.versioncontrol.logic.commands.RevertCommand;

/**
 * Parses input arguments and creates a new RevertCommand object
 */
public class RevertCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the RevertCommand
     * and returns a RevertCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RevertCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, RevertCommand.MESSAGE_USAGE));
        }

        return new RevertCommand(trimmedArgs);
    }
}
