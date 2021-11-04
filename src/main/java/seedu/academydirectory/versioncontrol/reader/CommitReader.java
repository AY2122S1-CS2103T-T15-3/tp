package seedu.academydirectory.versioncontrol.reader;

import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import seedu.academydirectory.versioncontrol.objects.Commit;

public class CommitReader extends VersionControlObjectReader<Commit> {
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private final TreeReader treeReader;

    /**
     * Creates a CommitReader to read Commit objects from disk
     * @param vcPath Path to read Commit objects from
     * @param treeReader TreeReader to build Tree objects to initialize Commit
     */
    public CommitReader(Path vcPath, TreeReader treeReader) {
        super(vcPath);
        this.treeReader = treeReader;
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
                fields.get(2), () -> this.read(fields.get(3)), () -> treeReader.read(fields.get(4)));
    }

    public DateFormat getDateFormat() {
        return df;
    }
}
