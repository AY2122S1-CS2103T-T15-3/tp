package seedu.academydirectory.logic.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.academydirectory.testutil.TypicalStudents.getTypicalAcademyDirectory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.academydirectory.model.AcademyDirectory;
import seedu.academydirectory.model.UserPrefs;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.VersionedModelManager;

public class HistoryCommandTest {
    private static final Path COMMAND_TEST_DIR = Paths.get("src", "test", "data",
            "VersionControlTest", "CommandTest");
    private static final AcademyDirectory TYPICAL_ACADEMY = getTypicalAcademyDirectory();
    private static final AcademyDirectory EMPTY_ACADEMY = new AcademyDirectory();
    private static final Supplier<Stream<AcademyDirectory>> academyDirectorySupplier = () -> Stream.of(
            TYPICAL_ACADEMY, EMPTY_ACADEMY);

    @Test
    public void execute_noVcFiles(@TempDir Path tempPath) {
        UserPrefs userPrefs = getTempUserPref(COMMAND_TEST_DIR.resolve("EmptyVCFolder"), tempPath);
        academyDirectorySupplier.get()
                .forEach((academyDirectory) -> {
                    VersionedModel model = new VersionedModelManager(academyDirectory, userPrefs);
                    Command historyCommand = new HistoryCommand();
                    CommandResult commandResult = assertDoesNotThrow(() -> historyCommand.execute(model));
                    assertEquals(HistoryCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
                    assertEquals("", model.getAdditionalViewModel().getAdditionalInfo().get());
                });
    }

    @Test
    public void execute_missingVcFolder(@TempDir Path tempPath) {
        UserPrefs userPrefs = getTempUserPref(tempPath.resolve("IMMMISSING"), tempPath);
        academyDirectorySupplier.get()
                .forEach((academyDirectory) -> {
                    VersionedModel model = new VersionedModelManager(academyDirectory, userPrefs);
                    Command historyCommand = new HistoryCommand();
                    CommandResult commandResult = assertDoesNotThrow(() -> historyCommand.execute(model));
                    assertEquals(HistoryCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
                    assertEquals("", model.getAdditionalViewModel().getAdditionalInfo().get());
                });
    }

    @Test
    public void execute_linearHistory(@TempDir Path tempPath) {
        // Guard clause since this integration test does not work in Windows CI due to lack of write permissions
        if (!tempPath.toFile().setWritable(true)) {
            return;
        }

        for (File file : Objects.requireNonNull(COMMAND_TEST_DIR.resolve("LinearHistory").toFile().listFiles())) {
            assertDoesNotThrow(() -> Files.copy(file.toPath(), tempPath.resolve(file.toPath().getFileName())));
        }

        UserPrefs userPrefs = getTempUserPref(tempPath, tempPath);
        String expectedResult = "| * 09726 - Sun, 7 Nov 2021 12:50:21 +0800 (HEAD) (MAIN)" + System.lineSeparator()
                + "| | \t\tDeleted Student: Charlotte Oliveiro" + System.lineSeparator()
                + "|/" + System.lineSeparator()
                + "* b1d61 - Sun, 7 Nov 2021 12:50:19 +0800 (OLD)" + System.lineSeparator()
                + "| \t\tDeleted Student: Bernice Yu" + System.lineSeparator()
                + "* f16ed - Sun, 7 Nov 2021 12:50:17 +0800 " + System.lineSeparator()
                + "| \t\tDeleted Student: Alex Yeoh" + System.lineSeparator()
                + "* b91d4 - Sun, 7 Nov 2021 12:50:04 +0800 " + System.lineSeparator()
                + "| \t\tInitial Commit";

        academyDirectorySupplier.get()
                .forEach(academyDirectory -> {
                    VersionedModel model = new VersionedModelManager(academyDirectory, userPrefs);
                    Command historyCommand = new HistoryCommand();
                    CommandResult commandResult = assertDoesNotThrow(() -> historyCommand.execute(model));
                    assertEquals(HistoryCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
                    assertDoesNotThrow(() -> model.getAdditionalViewModel().getAdditionalInfo().get());
                    assertEquals(expectedResult.length(), model.getAdditionalViewModel().getAdditionalInfo()
                            .get().toString().length());
                });
    }

    @Test
    public void execute_branchHistory(@TempDir Path tempPath) {
        // Guard clause since this integration test does not work in Windows CI due to lack of write permissions
        if (!tempPath.toFile().setWritable(true)) {
            return;
        }

        for (File file : Objects.requireNonNull(COMMAND_TEST_DIR.resolve("BranchHistory").toFile().listFiles())) {
            assertDoesNotThrow(() -> Files.copy(file.toPath(), tempPath.resolve(file.toPath().getFileName())));
        }

        UserPrefs userPrefs = getTempUserPref(tempPath, tempPath);

        String expectedResult = "| * a4856 - Sun, 7 Nov 2021 12:54:33 +0800 (HEAD) (MAIN)" + System.lineSeparator()
                + "| | \t\tDeleted Student: Irfan Ibrahim" + System.lineSeparator()
                + "* | 50499 - Sun, 7 Nov 2021 12:54:14 +0800 (OLD)" + System.lineSeparator()
                + "| | \t\tDeleted Student: David Li" + System.lineSeparator()
                + "* | fffd2 - Sun, 7 Nov 2021 12:54:10 +0800 " + System.lineSeparator()
                + "| | \t\tDeleted Student: Roy Balakrishnan" + System.lineSeparator()
                + "|/" + System.lineSeparator()
                + "* b1d61 - Sun, 7 Nov 2021 12:50:19 +0800 " + System.lineSeparator()
                + "| \t\tDeleted Student: Bernice Yu" + System.lineSeparator()
                + "* f16ed - Sun, 7 Nov 2021 12:50:17 +0800 " + System.lineSeparator()
                + "| \t\tDeleted Student: Alex Yeoh" + System.lineSeparator()
                + "* b91d4 - Sun, 7 Nov 2021 12:50:04 +0800 " + System.lineSeparator()
                + "| \t\tInitial Commit";

        academyDirectorySupplier.get()
                .forEach(academyDirectory -> {
                    VersionedModel model = new VersionedModelManager(academyDirectory, userPrefs);
                    Command historyCommand = new HistoryCommand();
                    CommandResult commandResult = assertDoesNotThrow(() -> historyCommand.execute(model));
                    assertEquals(HistoryCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
                    assertDoesNotThrow(() -> model.getAdditionalViewModel().getAdditionalInfo().get());
                    assertEquals(expectedResult, model.getAdditionalViewModel().getAdditionalInfo().get().toString());
                });
    }

    private static UserPrefs getTempUserPref(Path dataPath, Path vcPath) {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAcademyDirectoryFilePath(vcPath);
        userPrefs.setVersionControlPath(dataPath);
        return userPrefs;
    }
}
