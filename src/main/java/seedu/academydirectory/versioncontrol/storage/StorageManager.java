package seedu.academydirectory.versioncontrol.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.academydirectory.versioncontrol.objects.VcObject;

public abstract class StorageManager<T extends VcObject> {
    public static final String[] NULL_PARSE = null;
    protected final Path vcPath;

    /**
     * Creates a StorageManager to build
     * @param vcPath path to load and save files to
     */
    protected StorageManager(Path vcPath) {
        this.vcPath = vcPath;
    }

    protected abstract List<String> getWriteableFormat(T vcObject) throws IllegalArgumentException;
    protected abstract T getProgrammableFormat(List<String> vcObject);

    /**
     * Writes a given VcObject to file, with filename being the given name
     * @param name Filename
     * @param vcObject to be written to file
     * @throws IOException Unable to write to file
     */
    public void write(String name, T vcObject) throws IOException {
        Path commitPath = this.vcPath.resolve(Path.of(name));
        FileWriter writer = new FileWriter(String.valueOf(commitPath));

        List<String> writeableVcObject = this.getWriteableFormat(vcObject);
        writeableVcObject.forEach(x -> {
            try {
                writer.append(x).append(System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        writer.close();
    }

    /**
     * Reads a given VcObject from file, with filename being the given name
     */
    public T read(String name) {
        Path filePath = this.vcPath.resolve(Path.of(name));
        Optional<String[]> responseArr = loadFile(filePath).map(s -> s.split(System.lineSeparator()));
        return getProgrammableFormat(Stream.concat(Stream.of(name), Stream.of(responseArr.orElse(new String[]{})))
                .collect(Collectors.toList()));
    }

    private Optional<String> loadFile(Path filepath) {
        File file = new File(String.valueOf(filepath));
        String result;
        try {
            result = new String(new FileInputStream(file).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return Optional.empty();
        }
        return Optional.of(result);
    }
}
