package seedu.academydirectory.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.academydirectory.testutil.Assert.assertThrows;
import static seedu.academydirectory.testutil.TypicalStudents.ALICE;
import static seedu.academydirectory.testutil.TypicalStudents.HOON;
import static seedu.academydirectory.testutil.TypicalStudents.IDA;
import static seedu.academydirectory.testutil.TypicalStudents.getTypicalAcademyDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.academydirectory.commons.exceptions.DataConversionException;
import seedu.academydirectory.model.AcademyDirectory;
import seedu.academydirectory.model.ReadOnlyAcademyDirectory;

public class JsonAcademyDirectoryStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonAcademyDirectoryStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readAcademyDirectory_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readAcademyDirectory(null));
    }

    private java.util.Optional<ReadOnlyAcademyDirectory> readAcademyDirectory(String filePath) throws Exception {
        return new JsonAcademyDirectoryStorage(Paths.get(filePath))
                .readAcademyDirectory(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAcademyDirectory("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readAcademyDirectory("notJsonFormatAcademyDirectory.json"));
    }

    @Test
    public void readAcademyDirectory_invalidStudentAAcademyDirectory_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readAcademyDirectory("invalidStudentAcademyDirectory.json"));
    }

    @Test
    public void readAcademyDirectory_invalidAndValidStudentAcademyDirectory_throwDataConversionException() {
        assertThrows(DataConversionException.class, () ->
                readAcademyDirectory("invalidAndValidStudentAcademyDirectory.json"));
    }

    @Test
    public void readAndSaveAcademyDirectory_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempAcademyDirectory.json");
        AcademyDirectory original = getTypicalAcademyDirectory();
        JsonAcademyDirectoryStorage jsonAcademyDirectoryStorage = new JsonAcademyDirectoryStorage(filePath);

        // Save in new file and read_fileExist_correctTree back
        jsonAcademyDirectoryStorage.saveAcademyDirectory(original, filePath);
        ReadOnlyAcademyDirectory readBack = jsonAcademyDirectoryStorage.readAcademyDirectory(filePath).get();
        assertEquals(original, new AcademyDirectory(readBack));

        // Modify data, overwrite exiting file, and read_fileExist_correctTree back
        original.addStudent(HOON);
        original.removeStudent(ALICE);
        jsonAcademyDirectoryStorage.saveAcademyDirectory(original, filePath);
        readBack = jsonAcademyDirectoryStorage.readAcademyDirectory(filePath).get();
        assertEquals(original, new AcademyDirectory(readBack));

        // Save and read_fileExist_correctTree without specifying file path
        original.addStudent(IDA);
        jsonAcademyDirectoryStorage.saveAcademyDirectory(original); // file path not specified
        readBack = jsonAcademyDirectoryStorage.readAcademyDirectory().get(); // file path not specified
        assertEquals(original, new AcademyDirectory(readBack));

    }

    @Test
    public void saveAcademyDirectory_nullAcademyDirectory_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAcademyDirectory(null, "SomeFile.json"));
    }

    /**
     * Saves {@code academyDirectory} at the specified {@code filePath}.
     */
    private void saveAcademyDirectory(ReadOnlyAcademyDirectory academyDirectory, String filePath) {
        try {
            new JsonAcademyDirectoryStorage(Paths.get(filePath))
                    .saveAcademyDirectory(academyDirectory, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAcademyDirectory_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAcademyDirectory(new AcademyDirectory(), null));
    }
}
