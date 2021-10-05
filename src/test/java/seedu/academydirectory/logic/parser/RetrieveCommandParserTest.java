package seedu.academydirectory.logic.parser;
import static seedu.academydirectory.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.academydirectory.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.academydirectory.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.logic.commands.RetrieveCommand;
import seedu.academydirectory.model.student.InformationWantedFunction;

@SuppressWarnings("checkstyle:Regexp")
public class RetrieveCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, RetrieveCommand.MESSAGE_USAGE);

    private final RetrieveCommandParser parser = new RetrieveCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "i/", MESSAGE_INVALID_FORMAT);
        assertParseFailure(parser, "n/", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_validArgs_returnsRetrieveCommand() {
        String userInput = "e/";
        RetrieveCommand expectedCommand = new RetrieveCommand(new InformationWantedFunction(new Prefix(userInput)));
        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
