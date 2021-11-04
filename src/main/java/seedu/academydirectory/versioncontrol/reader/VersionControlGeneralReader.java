package seedu.academydirectory.versioncontrol.reader;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.nio.file.Path;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Label;
import seedu.academydirectory.versioncontrol.objects.Tree;

public class VersionControlGeneralReader {
    private final TreeReader treeReader;
    private final CommitReader commitReader;
    private final LabelReader labelReader;
    private final Path vcPath;

    /**
     * Provides a facade layer for reading {@code Tree}, {@code Commit}, and {@code Label}
     * @param vcPath Path to read files from
     */
    public VersionControlGeneralReader(Path vcPath) {
        this.treeReader = new TreeReader(vcPath);
        this.commitReader = new CommitReader(vcPath, treeReader);
        this.labelReader = new LabelReader(vcPath, commitReader);

        this.vcPath = vcPath;
    }

    /**
     * Reads a Tree whose hash matches the given hash. If there are multiple matches, the first matching file will
     * be returned
     * @param hash hash to be matched against
     * @return Tree whose hash matches the given hash, or an empty Tree if no matches found or if any issues
     * encountered
     */
    public Tree readTree(String hash) {
        requireNonNull(hash);

        // Allow for 5 digit hash to be used
        File[] matchingFiles = getMatchFile(hash);
        if (matchingFiles.length == 0) {
            return Tree.emptyTree();
        }

        // Pick first match
        Path filePath = matchingFiles[0].toPath();
        return treeReader.read(String.valueOf(filePath.getFileName()));
    }

    /**
     * Reads a Commit whose hash matches the given hash. If there are multiple matches, the first matching file will
     * be returned
     * @param hash hash to be matched against
     * @return Commit whose hash matches the given hash, or an empty Commit if no matches found or if any issues
     * encountered
     */
    public Commit readCommit(String hash) {
        requireNonNull(hash);

        // Allow for 5 digit hash to be used
        File[] matchingFiles = getMatchFile(hash);
        if (matchingFiles.length == 0) {
            return Commit.emptyCommit();
        }

        // Pick first match
        Path filePath = matchingFiles[0].toPath();
        return commitReader.read(String.valueOf(filePath.getFileName()));
    }

    /**
     * Reads a Label whose name matches the given name. If there are multiple matches, the first matching file will
     * be returned
     * @param name name to be matched against
     * @return Label whose name matches the given name, or an empty Label if no matches found or if any issues
     * encountered
     */
    public Label readLabel(String name) {
        requireNonNull(name);

        File[] matchingFiles;
        try {
            matchingFiles = getExactMatchFile(name);
            if (matchingFiles.length == 0) {
                return Label.emptyLabel();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            return Label.emptyLabel();
        }

        // Pick first match
        Path filePath = matchingFiles[0].toPath();
        return labelReader.read(String.valueOf(filePath.getFileName()));
    }

    private File[] getMatchFile(String string) {
        requireNonNull(string);

        File f = new File(String.valueOf(vcPath));
        return requireNonNull(f.listFiles((x, name) -> name.startsWith(string)));
    }

    private File[] getExactMatchFile(String string) {
        requireNonNull(string);

        File f = new File(String.valueOf(vcPath));
        return requireNonNull(f.listFiles((x, name) -> name.equals(string)));
    }
}
