package seedu.academydirectory.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.academydirectory.model.VersionedModel.PREDICATE_SHOW_ALL_STUDENTS;
import static seedu.academydirectory.testutil.Assert.assertThrows;
import static seedu.academydirectory.testutil.TypicalStudents.ALICE;
import static seedu.academydirectory.testutil.TypicalStudents.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.commons.core.GuiSettings;
import seedu.academydirectory.model.student.NameContainsKeywordsPredicate;
import seedu.academydirectory.testutil.AcademyDirectoryBuilder;

public class VersionedModelManagerTest {

    private VersionedModelManager versionedModelManager = new VersionedModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), versionedModelManager.getUserPrefs());
        assertEquals(new GuiSettings(), versionedModelManager.getGuiSettings());
        assertEquals(new AcademyDirectory(), new AcademyDirectory(versionedModelManager.getAcademyDirectory()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> versionedModelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAcademyDirectoryFilePath(Paths.get("academy/directory/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        versionedModelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, versionedModelManager.getUserPrefs());

        // Modifying userPrefs should not modify versionedModelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAcademyDirectoryFilePath(Paths.get("new/academy/directory/file/path"));
        assertEquals(oldUserPrefs, versionedModelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> versionedModelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        versionedModelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, versionedModelManager.getGuiSettings());
    }

    @Test
    public void setAcademyDirectoryFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> versionedModelManager.setAcademyDirectoryFilePath(null));
    }

    @Test
    public void setAcademyDirectoryFilePath_validPath_setsAcademyDirectoryFilePath() {
        Path path = Paths.get("academy/directory/file/path");
        versionedModelManager.setAcademyDirectoryFilePath(path);
        assertEquals(path, versionedModelManager.getAcademyDirectoryFilePath());
    }

    @Test
    public void hasStudent_nullStudent_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> versionedModelManager.hasStudent(null));
    }

    @Test
    public void hasStudent_studentNotInAcademyDirectory_returnsFalse() {
        assertFalse(versionedModelManager.hasStudent(ALICE));
    }

    @Test
    public void hasStudent_studentInAcademyDirectory_returnsTrue() {
        versionedModelManager.addStudent(ALICE);
        assertTrue(versionedModelManager.hasStudent(ALICE));
    }

    @Test
    public void getFilteredStudentList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> versionedModelManager
                .getFilteredStudentList().remove(0));
    }

    @Test
    public void equals() {
        AcademyDirectory academyDirectory = new AcademyDirectoryBuilder().withStudent(ALICE)
                .withStudent(BENSON).build();
        AcademyDirectory differentAcademyDirectory = new AcademyDirectory();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        versionedModelManager = new VersionedModelManager(academyDirectory, userPrefs);
        VersionedModelManager versionedModelManagerCopy = new VersionedModelManager(academyDirectory, userPrefs);
        assertTrue(versionedModelManager.equals(versionedModelManagerCopy));

        // same object -> returns true
        assertTrue(versionedModelManager.equals(versionedModelManager));

        // null -> returns false
        assertFalse(versionedModelManager.equals(null));

        // different types -> returns false
        assertFalse(versionedModelManager.equals(5));

        // different academyDirectory -> returns false
        assertFalse(versionedModelManager.equals(new VersionedModelManager(differentAcademyDirectory, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        versionedModelManager.updateFilteredStudentList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(versionedModelManager.equals(new VersionedModelManager(academyDirectory, userPrefs)));

        // resets versionedModelManager to initial state for upcoming tests
        versionedModelManager.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAcademyDirectoryFilePath(Paths.get("differentFilePath"));
        assertFalse(versionedModelManager.equals(new VersionedModelManager(academyDirectory, differentUserPrefs)));
    }
}
