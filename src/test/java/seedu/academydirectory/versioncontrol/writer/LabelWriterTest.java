package seedu.academydirectory.versioncontrol.writer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT1;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT3;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.academydirectory.versioncontrol.objects.Label;

public class LabelWriterTest {
    @TempDir
    public Path tempPath;
    private LabelWriter labelWriter;

    @Test
    public void getWriteableFormat() {
        labelWriter = new LabelWriter(tempPath);

        // Correct Labels
        Label label1 = Label.of("Test1", "Test1", () -> COMMIT1);
        Label label2 = Label.of("Test2", "Test2", () -> COMMIT3);

        assertEquals(List.of("ref: 1d83638a25901e76c8e3882afca2347f8352cd06"),
                labelWriter.getWriteableFormat(label1));
        assertEquals(List.of("ref: 2"),
                labelWriter.getWriteableFormat(label2));

        // Negative Test: null
        assertThrows(NullPointerException.class, () -> labelWriter.getWriteableFormat(null));
        assertThrows(IllegalArgumentException.class, () -> labelWriter.getWriteableFormat(Label.emptyLabel()));
    }

    @Test
    public void write() {
        labelWriter = new LabelWriter(tempPath);

        // Correct Labels
        Label label1 = Label.of("Test1", "Test1", () -> COMMIT1);
        Label label2 = Label.of("Test2", "Test2", () -> COMMIT3);

        assertDoesNotThrow(() -> labelWriter.write(label1.getName(), label1));
        assertDoesNotThrow(() -> labelWriter.write(label2.getName(), label2));
        assertDoesNotThrow(() -> labelWriter.write(label1.getName(), label2));

        // Negative Test: null
        assertThrows(NullPointerException.class, () -> labelWriter.write(label1.getName(), null));
        assertThrows(NullPointerException.class, () -> labelWriter.write(null, label1));
        assertThrows(NullPointerException.class, () -> labelWriter.write(null, Label.emptyLabel()));

        assertThrows(IllegalArgumentException.class, () -> labelWriter.write(label1.getName(), Label.emptyLabel()));
    }
}
