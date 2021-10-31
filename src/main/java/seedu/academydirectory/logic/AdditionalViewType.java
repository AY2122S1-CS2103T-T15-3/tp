package seedu.academydirectory.logic;

import seedu.academydirectory.logic.commands.Command;
import seedu.academydirectory.logic.commands.GetCommand;
import seedu.academydirectory.logic.commands.HelpCommand;
import seedu.academydirectory.logic.commands.HistoryCommand;
import seedu.academydirectory.logic.commands.ShowCommand;
import seedu.academydirectory.logic.commands.ViewCommand;
import seedu.academydirectory.logic.commands.VisualizeCommand;

public enum AdditionalViewType {
    DEFAULT, HELP, VIEW, VISUALIZE, TEXT;

    /**
     * Returns the appropriate AdditionalViewType according to the given Command
     * @param command Command whose view type is to be determined
     * @return AdditionalViewType of the corresponding command
     */
    public static AdditionalViewType parse(Command command) {
        if (command instanceof HelpCommand) {
            return AdditionalViewType.HELP;
        } else if (command instanceof ViewCommand) {
            return AdditionalViewType.VIEW;
        } else if (command instanceof VisualizeCommand) {
            return AdditionalViewType.VISUALIZE;
        } else if (command instanceof HistoryCommand || command instanceof GetCommand
               || command instanceof ShowCommand) {
            return AdditionalViewType.TEXT;
        } else {
            return AdditionalViewType.DEFAULT;
        }
    }
}
