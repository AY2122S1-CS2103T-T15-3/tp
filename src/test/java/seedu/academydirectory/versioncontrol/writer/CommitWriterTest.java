package seedu.academydirectory.versioncontrol.writer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT1;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT3;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT9;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.academydirectory.versioncontrol.objects.Commit;

public class CommitWriterTest {
    @TempDir
    public Path tempPath;

    private CommitWriter commitWriter;

    @Test
    public void getWriteableFormat_positiveTests() {
        commitWriter = new CommitWriter(tempPath);

        // Missing Parent and TreeRef
        List<String> writeableForm = commitWriter.getWriteableFormat(COMMIT1);
        assertEquals("Author: " + System.getProperty("user.name"), writeableForm.stream().limit(1)
                .reduce((a, b) -> a + b).orElse(null));
        assertEquals(List.of("Date: 31/12/1998 00:00:00", "Message: Hello, World!", "Parent: null",
                "TreeRef: null"), writeableForm.stream().skip(1).collect(Collectors.toList()));

        // Parent present but missing TreeRef
        writeableForm = commitWriter.getWriteableFormat(COMMIT3);
        assertEquals("Author: " + System.getProperty("user.name"), writeableForm.stream().limit(1)
                .reduce((a, b) -> a + b).orElse(null));
        assertEquals(List.of("Date: 31/12/1998 00:00:00", "Message: This is Second Commit",
                        "Parent: 1",
                        "TreeRef: null"),
                writeableForm.stream().skip(1).collect(Collectors.toList()));

        // Parent present and TreeRef present
        writeableForm = commitWriter.getWriteableFormat(COMMIT9);
        assertEquals("Author: " + System.getProperty("user.name"), writeableForm.stream().limit(1)
                .reduce((a, b) -> a + b).orElse(null));
        assertEquals(List.of("Date: 31/12/1998 00:00:00",
                        "Message: This is second element of linked list",
                        "Parent: 1",
                        "TreeRef: 9d34f3e9ada5ae7cc5c063b905a5d7893f792497"),
                writeableForm.stream().skip(1).collect(Collectors.toList()));
    }

    @Test
    public void getWriteableFormat_negativeTests() {
        commitWriter = new CommitWriter(tempPath);

        // Negative Tests
        assertThrows(NullPointerException.class, () -> commitWriter.getWriteableFormat(null));
        assertThrows(IllegalArgumentException.class, () -> commitWriter.getWriteableFormat(
                Commit.emptyCommit()));
    }

    @Test
    public void write() {
        commitWriter = new CommitWriter(tempPath);

        // Correct Trees
        assertDoesNotThrow(() -> commitWriter.write(COMMIT1.getHash(), COMMIT1));
        assertDoesNotThrow(() -> commitWriter.write(COMMIT1.getHash(), COMMIT3));
        assertDoesNotThrow(() -> commitWriter.write(COMMIT3.getHash(), COMMIT9));

        // Negative Test: null
        assertThrows(NullPointerException.class, () -> commitWriter.write(COMMIT1.getHash(), null));
        assertThrows(NullPointerException.class, () -> commitWriter.write(null, COMMIT1));
        assertThrows(NullPointerException.class, () -> commitWriter.write(null, Commit.emptyCommit()));

        assertThrows(IllegalArgumentException.class, () -> commitWriter.write(COMMIT1.getHash(), Commit.emptyCommit()));
    }
}
