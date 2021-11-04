package seedu.academydirectory.versioncontrol.reader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import seedu.academydirectory.versioncontrol.objects.Tree;

public class TreeReader extends VersionControlObjectReader<Tree> {
    public TreeReader(Path vcPath) {
        super(vcPath);
    }

    @Override
    protected Tree getProgrammableFormat(List<String> responseArr) {
        assert responseArr.size() >= 1; // Guaranteed by write() function
        if (responseArr.size() == 1) {
            return Tree.emptyTree(); // Corrupted read => return null tree
        }
        String hash = responseArr.get(0);
        Iterator<String> args = responseArr.listIterator(1);

        List<String> vcNames = new ArrayList<>();
        List<String> realNames = new ArrayList<>();
        while (args.hasNext()) {
            String[] arg = args.next().split(" ");
            if (arg.length != 2) {
                return Tree.emptyTree();
            }
            vcNames.add(arg[0]);
            realNames.add(arg[1]);
        }

        return Tree.of(hash, realNames, vcNames);
    }
}
