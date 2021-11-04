package seedu.academydirectory.versioncontrol.writer;

import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import seedu.academydirectory.versioncontrol.objects.Commit;

public class CommitWriter extends VersionControlObjectWriter<Commit> {
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    /**
     * Creates a StorageManager for Commit that is responsible for reading and writing Commit objects from and to disk
     * @param vcPath Path to read and write objects
     */
    public CommitWriter(Path vcPath) {
        super(vcPath);
    }

    @Override
    protected List<String> getWriteableFormat(Commit commit) {
        if (commit.isEmpty()) {
            throw new IllegalArgumentException("Cannot get writeable format of NULL!");
        }

        String author = "Author: " + System.getProperty("user.name");
        String date = "Date: " + df.format(commit.getDate());
        String message = "Message: " + commit.getMessage();
        String parent = "Parent: " + commit.getParentSupplier().get().getHash();
        String treeRef = "TreeRef: " + commit.getTreeSupplier().get().getHash();

        return List.of(author, date, message, parent, treeRef);
    }
}
