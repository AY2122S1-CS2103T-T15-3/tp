package seedu.academydirectory.logic.parser;

import static seedu.academydirectory.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.academydirectory.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.academydirectory.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.academydirectory.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.academydirectory.testutil.TypicalStudents.getTypicalStudents;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.logic.commands.GetCommand;
import seedu.academydirectory.model.student.Name;
import seedu.academydirectory.model.student.NameContainsKeywordsPredicate;

@SuppressWarnings("checkstyle:Regexp")
public class GetCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, GetCommand.MESSAGE_USAGE);

    private final GetCommandParser parser = new GetCommandParser();

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
    public void parse_validArgs_returnsGetCommand() {
        Stream<Prefix> relevantPrefixes = Stream.of(GetCommand.SUPPORTED_PREFIX.toArray(Prefix[]::new))
                .parallel();
        relevantPrefixes.forEach(prefix -> {
            String userInput = " " + prefix.getPrefix();
            GetCommand expectedCommand = new GetCommand(List.of(prefix), List.of());
            assertParseSuccess(parser, userInput, expectedCommand);
        });
    }

    @Test
    public void parse_validArgsWithName_returnsGetCommand() {
        Name name = getTypicalStudents().get(0).getName();

        Stream<Prefix> relevantPrefixes = Stream.of(GetCommand.SUPPORTED_PREFIX.toArray(Prefix[]::new))
                .parallel();
        relevantPrefixes.forEach(prefix -> {
            String userInput = " " + prefix.getPrefix() + " " + PREFIX_NAME + name.fullName;
            NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of(name.fullName));
            GetCommand expectedCommand = new GetCommand(List.of(prefix), List.of(name.fullName));
            assertParseSuccess(parser, userInput, expectedCommand);
        });
    }
}
