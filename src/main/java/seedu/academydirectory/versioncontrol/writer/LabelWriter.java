package seedu.academydirectory.versioncontrol.writer;

import java.nio.file.Path;
import java.util.List;

import seedu.academydirectory.versioncontrol.objects.Label;

public class LabelWriter extends VersionControlObjectWriter<Label> {

    public LabelWriter(Path vcPath) {
        super(vcPath);
    }

    @Override
    public List<String> getWriteableFormat(Label label) throws IllegalArgumentException {
        if (label.equals(Label.emptyLabel())) {
            throw new IllegalArgumentException("Cannot get writeable format of NULL!");
        }
        return List.of("ref: " + label.getCommitSupplier().get().getHash());
    }
}
