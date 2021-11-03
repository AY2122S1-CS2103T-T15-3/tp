package seedu.academydirectory.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.academydirectory.commons.core.GuiSettings;
import seedu.academydirectory.logic.AdditionalViewType;
import seedu.academydirectory.model.student.Student;

/**
 * The API of the VersionedModel component.
 */
public interface VersionedModel extends Version {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Student> PREDICATE_SHOW_ALL_STUDENTS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' academy directory file path.
     */
    Path getAcademyDirectoryFilePath();

    /**
     * Sets the user prefs' academy directory file path.
     */
    void setAcademyDirectoryFilePath(Path academyDirectoryFilePath);

    /**
     * Replaces academy directory data with the data in {@code academyDirectory}.
     */
    void setAcademyDirectory(ReadOnlyAcademyDirectory academyDirectory);

    /** Returns the AcademyDirectory */
    ReadOnlyAcademyDirectory getAcademyDirectory();

    /**
     * Returns true if a student with the same identity as {@code student} exists in the academy directory.
     */
    boolean hasStudent(Student student);

    /**
     * Deletes the given student.
     * The student must exist in the academy directory.
     */
    void deleteStudent(Student target);

    /**
     * Adds the given student.
     * {@code student} must not already exist in the academy directory.
     */
    void addStudent(Student student);

    /**
     * Replaces the given student {@code target} with {@code editedStudent}.
     * {@code target} must exist in the academy directory.
     * The student identity of {@code editedStudent} must not be the same
     * as another existing student in the academy directory.
     */
    void setStudent(Student target, Student editedStudent);

    /** Returns an unmodifiable view of the filtered student list */
    ObservableList<Student> getFilteredStudentList();

    /**
     * Updates the filter of the filtered student list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredStudentList(Predicate<Student> predicate);

    AdditionalViewModel getAdditionalViewModel();

    void setAdditionalViewType(AdditionalViewType additionalViewType);

    void setAdditionalInfo(AdditionalInfo<? extends Object> additionalInfo);
}
