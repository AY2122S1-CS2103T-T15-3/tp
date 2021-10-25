package seedu.academydirectory.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {
    private static final String DEFAULT_HELP = "### None";

    private final String feedbackToUser;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;

    /** What the message on the help window should be. */
    private final String helpContent;

    /** What commit message should be used, if any */
    private final Optional<String> commitMessage;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.helpContent = DEFAULT_HELP;
        this.showHelp = showHelp;
        this.exit = exit;
        this.commitMessage = Optional.empty();
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit, Optional<String> commitMessage) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.helpContent = DEFAULT_HELP;
        this.showHelp = showHelp;
        this.exit = exit;
        this.commitMessage = commitMessage;
    }

    /**
     * Alternative constructor of CommandResult, creating a {@code CommandResult}, this time taking
     * another help message in.
     */
    public CommandResult(String feedbackToUser, String helpContent) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.helpContent = requireNonNull(helpContent);
        this.showHelp = true;
        this.exit = false;
        this.commitMessage = Optional.empty();
    }

    /**
     * Alternative constructor of CommandResult with commitMessage support
     */
    public CommandResult(String feedbackToUser, String helpContent, Optional<String> commitMessage) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.helpContent = requireNonNull(helpContent);
        this.showHelp = true;
        this.exit = false;
        this.commitMessage = commitMessage;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser, Optional<String> commitMessage) {
        this(feedbackToUser, false, false, commitMessage);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public String getHelpContent() {
        return helpContent;
    }

    public Optional<String> getCommitMessage() {
        return commitMessage;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && helpContent.equals(otherCommandResult.helpContent)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit);
    }
}
