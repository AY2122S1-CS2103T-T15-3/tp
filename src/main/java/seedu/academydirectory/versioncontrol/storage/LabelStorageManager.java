package seedu.academydirectory.versioncontrol.storage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import seedu.academydirectory.versioncontrol.objects.Label;

public class LabelStorageManager extends StorageManager<Label> {
    private final CommitStorageManager commitStorageManager;

    /**
     * Creates a StorageManager for Label that is responsible for reading and writing Label objects from and to disk
     * @param vcPath Path to read and write objects
     * @param commitStorageManager CommitStorageManager to build Commit objects to initialize label
     */
    public LabelStorageManager(Path vcPath, CommitStorageManager commitStorageManager) {
        super(vcPath);
        this.commitStorageManager = commitStorageManager;
    }

    @Override
    public List<String> getWriteableFormat(Label label) throws IllegalArgumentException {
        if (label.equals(Label.emptyLabel())) {
            throw new IllegalArgumentException("Cannot get writeable format of NULL!");
        }
        return List.of("ref: " + label.getCommitSupplier().get().getHash());
    }

    @Override
    protected Label getProgrammableFormat(List<String> responseArr) {
        assert responseArr.size() >= 1; // Will contain at least filename
        String name = responseArr.get(0);
        Iterator<String> args = responseArr.listIterator(1);

        List<String> fields = new ArrayList<>();
        while (args.hasNext()) {
            String[] arg = args.next().split(": ");
            if (arg.length == 2) {
                fields.add(arg[1]);
            }
        }

        if (fields.size() != 1) {
            return Label.emptyLabel();
        }

        return Label.of(name, name, () -> commitStorageManager.read(fields.get(0)));
    }
}
