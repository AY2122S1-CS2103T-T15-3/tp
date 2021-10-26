package seedu.academydirectory.logic.commands;

import static java.util.Objects.requireNonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.academydirectory.logic.AdditionalViewType;
import seedu.academydirectory.logic.commands.exceptions.CommandException;
import seedu.academydirectory.model.AdditionalInfo;
import seedu.academydirectory.model.VersionControl;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.versioncontrol.objects.Commit;

/**
 * Lists all commands used in the academy directory to the user.
 */
public class HistoryCommand extends Command {
    public static final String COMMAND_WORD = "history";
    public static final String MESSAGE_SUCCESS = "Commit history shown";
    public static final String HELP_MESSAGE = "### Listing all history : `history`\n"
            + "\n"
            + "Shows a list of all command history in the academy directory.\n"
            + "\n"
            + "Format: `history`";

    private static final SimpleDateFormat DF = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");

    @Override
    public CommandResult execute(VersionedModel model) throws CommandException {
        requireNonNull(model);
        String result = String.join("\n", retrieveHistory(model));

        model.setAdditionalViewType(AdditionalViewType.HISTORY);
        model.setAdditionalInfo(AdditionalInfo.of(result));
        return new CommandResult(MESSAGE_SUCCESS);
    }

    private List<String> retrieveHistory(VersionedModel model) {
        Commit headCommit = model.getHeadCommit();;
        Commit latestCommit = model.fetchCommitByLabel(VersionControl.OLD_LABEL_STRING);

        Commit lca = headCommit.findLca(latestCommit);

        Commit latestAncestor = latestCommit.getHighestAncestor(lca);
        Commit headAncestor = headCommit.getHighestAncestor(lca);
        assert !headAncestor.equals(latestAncestor); // Violates LCA definition

        List<Commit> earlyHistory = lca.getHistory();
        List<Commit> latestToEarly = latestCommit.getHistory(lca);
        List<Commit> headToEarly = headCommit.getHistory(lca);

        List<Commit> sortedBranch = Stream.concat(latestToEarly.stream(), headToEarly.stream())
                .sorted(Comparator.comparing(Commit::getDate)).collect(Collectors.toList());
        List<Commit> sortedEarlyHistory = earlyHistory.stream().sorted(Comparator.comparing(Commit::getDate))
                .collect(Collectors.toList());

        List<String> result = new ArrayList<>();
        for (Commit commit : sortedEarlyHistory) {
            result.add("| " + getPresentableHistory(commit, 1, ""));
            result.add("* " + getPresentableHistory(commit, 0, commit.equals(headCommit) ? "(HEAD)" : ""));
        }

        if (sortedBranch.size() == 0) {
            Collections.reverse(result);
            return result;
        }

        result.add("|/"); // Separates early history from branch

        // Latest on left lane
        for (Commit commit : sortedBranch) {
            if (latestToEarly.contains(commit)) {
                result.add("| | " + getPresentableHistory(commit, 1, ""));
                result.add("* | " + getPresentableHistory(commit, 0, commit.equals(latestCommit) ? "(PRIOR)" : ""));
            } else {
                result.add("| | " + getPresentableHistory(commit, 1, ""));
                result.add("| * " + getPresentableHistory(commit, 0, commit.equals(headCommit) ? "(HEAD)" : ""));
            }
        }
        Collections.reverse(result);
        return result;
    }

    private String getPresentableHistory(Commit commit, int idx, String label) {
        assert idx == 0 || idx == 1;
        if (idx == 0) {
            return commit.getHash().substring(0, 5) + " - " + DF.format(commit.getDate()) + " " + label;
        } else {
            return "\t\t" + commit.getMessage();
        }
    }
}
