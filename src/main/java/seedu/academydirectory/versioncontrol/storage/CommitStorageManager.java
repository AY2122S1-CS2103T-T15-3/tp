package seedu.academydirectory.versioncontrol.storage;

import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import seedu.academydirectory.versioncontrol.objects.Commit;

public class CommitStorageManager extends StorageManager<Commit> {
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private final TreeStorageManager treeStorageManager;

    /**
     * Creates a StorageManager for Commit that is responsible for reading and writing Commit objects from and to disk
     * @param vcPath Path to read and write objects
     * @param treeStorageManager TreeStorageManager to build Tree objects to initialize Commit
     */
    public CommitStorageManager(Path vcPath, TreeStorageManager treeStorageManager) {
        super(vcPath);
        this.treeStorageManager = treeStorageManager;
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

    @Override
    protected Commit getProgrammableFormat(List<String> responseArr) {
        assert responseArr.size() >= 1; // Will contain at least filename
        String hash = responseArr.get(0);
        Iterator<String> args = responseArr.listIterator(1);

        List<String> fields = new ArrayList<>();
        while (args.hasNext()) {
            String[] arg = args.next().split(":", 2);
            fields.add(arg.length == 2 ? arg[1].trim() : "");
        }

        if (fields.size() != 5) {
            return Commit.emptyCommit();
        }

        Date date;
        try {
            date = df.parse(fields.get(1));
        } catch (ParseException e) {
            return Commit.emptyCommit();
        }

        return Commit.of(hash, fields.get(0), date,
                fields.get(2), () -> this.read(fields.get(3)), () -> treeStorageManager.read(fields.get(4)));
    }

    public DateFormat getDateFormat() {
        return df;
    }
}
