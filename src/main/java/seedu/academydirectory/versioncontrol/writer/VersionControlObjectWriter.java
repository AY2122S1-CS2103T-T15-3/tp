package seedu.academydirectory.versioncontrol.writer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import seedu.academydirectory.commons.util.FileUtil;
import seedu.academydirectory.versioncontrol.objects.VcObject;

public abstract class VersionControlObjectWriter<T extends VcObject> {
    protected final Path vcPath;

    /**
     * Creates a VersionControlObjectWriter to build
     * @param vcPath path to load and save files to
     */
    protected VersionControlObjectWriter(Path vcPath) {
        this.vcPath = vcPath;
    }

    /**
     * Writes a given VcObject to file, with filename being the given name
     * @param name Filename
     * @param vcObject to be written to file
     * @throws IOException Unable to write to file
     */
    public void write(String name, T vcObject) throws IOException {
        Path commitPath = this.vcPath.resolve(Path.of(name));
        FileUtil.writeToFile(commitPath, String.join(System.lineSeparator(), this.getWriteableFormat(vcObject)));
    }

    protected abstract List<String> getWriteableFormat(T vcObject) throws IllegalArgumentException;
}
