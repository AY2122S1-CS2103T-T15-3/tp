package seedu.academydirectory.logic.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.academydirectory.testutil.TypicalStudents.getTypicalAcademyDirectory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.academydirectory.commons.util.FileUtil;
import seedu.academydirectory.logic.commands.exceptions.CommandException;
import seedu.academydirectory.model.AcademyDirectory;
import seedu.academydirectory.model.UserPrefs;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.VersionedModelManager;
import seedu.academydirectory.versioncontrol.objects.Commit;

public class RevertCommandTest {
    private static final Path COMMAND_TEST_DIR = Paths.get("src", "test", "data",
            "VersionControlTest", "VersionControlTest", "Revert");
    private static final AcademyDirectory TYPICAL_ACADEMY = getTypicalAcademyDirectory();

    @Test
    public void execute_noVcFiles_commandException(@TempDir Path tempPath) {
        // Guard clause since this integration test does not work in Windows CI due to lack of write permissions
        if (!tempPath.toFile().setWritable(true)) {
            return;
        }
        for (File file : Objects.requireNonNull(COMMAND_TEST_DIR.toFile().listFiles())) {
            assertDoesNotThrow(() -> Files.copy(file.toPath(), tempPath.resolve(file.toPath().getFileName())));
        }
        UserPrefs userPrefs = getTempUserPref(tempPath, tempPath);
        VersionedModel model = new VersionedModelManager(TYPICAL_ACADEMY, userPrefs);
        Command revertCommand = new RevertCommand("NoSuchFile");
        assertThrows(CommandException.class, () -> revertCommand.execute(model));
    }

    @Test
    public void execute_revertCurrentHash_commandException(@TempDir Path tempPath) {
        // Guard clause since this integration test does not work in Windows CI due to lack of write permissions
        if (!tempPath.toFile().setWritable(true)) {
            return;
        }
        for (File file : Objects.requireNonNull(COMMAND_TEST_DIR.toFile().listFiles())) {
            assertDoesNotThrow(() -> Files.copy(file.toPath(), tempPath.resolve(file.toPath().getFileName())));
        }
        UserPrefs userPrefs = getTempUserPref(tempPath, tempPath);
        VersionedModel model = new VersionedModelManager(TYPICAL_ACADEMY, userPrefs);
        Command revertCommand = new RevertCommand(model.getHeadCommit().getHash());
        assertThrows(CommandException.class, () -> revertCommand.execute(model));
    }

    @Test
    public void execute_revertCorrectHash(@TempDir Path tempPath) {
        // Guard clause since this integration test does not work in Windows CI due to lack of write permissions
        if (!tempPath.toFile().setWritable(true)) {
            return;
        }
        for (File file : Objects.requireNonNull(COMMAND_TEST_DIR.toFile().listFiles())) {
            assertDoesNotThrow(() -> Files.copy(file.toPath(), tempPath.resolve(file.toPath().getFileName())));
        }

        // Tree will regenerate blob to the following path:
        Path regeneratePath = COMMAND_TEST_DIR.resolve("temp").resolve("academydirectory.json");
        if (FileUtil.isFileExists(regeneratePath)) {
            assertTrue(regeneratePath.toFile().delete());
        }

        Path dataDir = tempPath.resolve(Paths.get("Data"));
        assertDoesNotThrow(() -> FileUtil.createFile(dataDir));
        UserPrefs userPrefs = getTempUserPref(dataDir, tempPath);
        VersionedModel model = new VersionedModelManager(TYPICAL_ACADEMY, userPrefs);

        String correctHash = "6b8dca90ac26ec6f2f4fc3b7f820bc57f462fcf9";
        Command revertCommand = new RevertCommand(correctHash);
        assertDoesNotThrow(() -> revertCommand.execute(model));

        Commit revertedCommit = assertDoesNotThrow(model::getHeadCommit);

        assertFalse(revertedCommit.isEmpty());
        assertEquals(correctHash, revertedCommit.getHash());
    }

    @Test
    public void execute_noReadAccess(@TempDir Path tempPath) {
        // Guard clause since this integration test does not work in Windows CI due to lack of write permissions
        if (!tempPath.toFile().setWritable(true)) {
            return;
        }
        for (File file : Objects.requireNonNull(COMMAND_TEST_DIR.toFile().listFiles())) {
            assertDoesNotThrow(() -> Files.copy(file.toPath(), tempPath.resolve(file.toPath().getFileName())));
        }

        // Tree will regenerate blob to the following path:
        Path regeneratePath = COMMAND_TEST_DIR.resolve("temp").resolve("academydirectory.json");
        if (FileUtil.isFileExists(regeneratePath)) {
            assertTrue(regeneratePath.toFile().delete());
        }

        Path dataDir = tempPath.resolve(Paths.get("Data"));
        assertDoesNotThrow(() -> FileUtil.createFile(dataDir));
        UserPrefs userPrefs = getTempUserPref(dataDir, tempPath);
        VersionedModel model = new VersionedModelManager(TYPICAL_ACADEMY, userPrefs);

        String correctHash = "6b8dca90ac26ec6f2f4fc3b7f820bc57f462fcf9";
        Command revertCommand = new RevertCommand(correctHash);

        assertTrue(tempPath.toFile().setReadable(false));
        assertThrows(CommandException.class, () -> revertCommand.execute(model));
        assertTrue(() -> tempPath.toFile().setReadable(true));
    }

    @Test
    public void execute_cannotRegenerate(@TempDir Path tempPath) {
        // Guard clause since this integration test does not work in Windows CI due to lack of write permissions
        if (!tempPath.toFile().setWritable(true)) {
            return;
        }
        for (File file : Objects.requireNonNull(COMMAND_TEST_DIR.toFile().listFiles())) {
            assertDoesNotThrow(() -> Files.copy(file.toPath(), tempPath.resolve(file.toPath().getFileName())));
        }

        // Tree will regenerate blob to the following path:
        Path regeneratePath = COMMAND_TEST_DIR.resolve("temp").resolve("academydirectory.json");
        if (FileUtil.isFileExists(regeneratePath)) {
            assertTrue(regeneratePath.toFile().delete());
        }

        Path dataDir = tempPath.resolve(Paths.get("Data"));
        assertDoesNotThrow(() -> FileUtil.createFile(dataDir));
        UserPrefs userPrefs = getTempUserPref(dataDir, tempPath);
        VersionedModel model = new VersionedModelManager(TYPICAL_ACADEMY, userPrefs);

        String correctHash = "6b8dca90ac26ec6f2f4fc3b7f820bc57f462fcf9";
        Command revertCommand = new RevertCommand(correctHash);

        assertTrue(COMMAND_TEST_DIR.toFile().setReadable(false));
        assertDoesNotThrow(() -> revertCommand.execute(model));
        assertEquals(new AcademyDirectory(), model.getAcademyDirectory());
        assertTrue(() -> COMMAND_TEST_DIR.toFile().setReadable(true));
    }

    private static UserPrefs getTempUserPref(Path path, Path tempPath) {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAcademyDirectoryFilePath(path);
        userPrefs.setVersionControlPath(tempPath);
        return userPrefs;
    }
}
