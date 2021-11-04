package seedu.academydirectory.versioncontrol.writer;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import seedu.academydirectory.versioncontrol.objects.Tree;

public class TreeWriter extends VersionControlObjectWriter<Tree> {
    public TreeWriter(Path vcPath) {
        super(vcPath);
    }

    @Override
    public List<String> getWriteableFormat(Tree tree) {
        if (tree.isEmpty()) {
            throw new IllegalArgumentException("Cannot get writeable format of NULL!");
        }

        HashMap<String, String> hashMap = tree.getHashMap();
        return hashMap.keySet().stream()
                .map(key -> key + " " + hashMap.get(key))
                .collect(Collectors.toList());
    }
}
