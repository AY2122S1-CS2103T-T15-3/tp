package seedu.academydirectory.logic.parser;

import static seedu.academydirectory.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.academydirectory.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.academydirectory.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.logic.commands.GetCommand;
import seedu.academydirectory.model.student.InformationWantedFunction;

@SuppressWarnings("checkstyle:Regexp")
public class GetCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, GetCommand.MESSAGE_USAGE);

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
        Stream<Prefix> relevantPrefixes = Stream.of(InformationWantedFunction.SUPPORTED_PREFIX.toArray(Prefix[]::new))
                .parallel();
        relevantPrefixes.forEach(prefix -> {
            String userInput = " " + prefix.getPrefix();
            GetCommand expectedCommand = new GetCommand(new InformationWantedFunction(prefix));
            assertParseSuccess(parser, userInput, expectedCommand);
        });
    }
}
