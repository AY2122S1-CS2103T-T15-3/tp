package seedu.academydirectory.model;

import static java.util.Objects.requireNonNull;
import static seedu.academydirectory.commons.util.CollectionUtil.requireAllNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.academydirectory.commons.core.GuiSettings;
import seedu.academydirectory.commons.core.LogsCenter;
import seedu.academydirectory.logic.AdditionalViewType;
import seedu.academydirectory.model.student.Student;
import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.utils.HashMethod;

/**
 * Represents the in-memory model of the academy directory data.
 */
public class VersionedModelManager implements VersionedModel {
    private static final Logger logger = LogsCenter.getLogger(VersionedModelManager.class);

    private final AcademyDirectory academyDirectory;
    private final UserPrefs userPrefs;
    private final FilteredList<Student> filteredStudents;

    private final VersionControlController versionControlController;

    private final AdditionalViewModel additionalViewModel;

    /**
     * Initializes a VersionedModelManager with the given academyDirectory and userPrefs.
     */
    public VersionedModelManager(ReadOnlyAcademyDirectory academyDirectory, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(academyDirectory, userPrefs);

        logger.fine("Initializing with academy directory: " + academyDirectory + " and user prefs " + userPrefs);

        this.academyDirectory = new AcademyDirectory(academyDirectory);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredStudents = new FilteredList<>(this.academyDirectory.getStudentList());

        this.versionControlController = new VersionControlController(HashMethod.SHA1,
                userPrefs.getVersionControlPath(),
                userPrefs.getAcademyDirectoryFilePath());

        this.additionalViewModel = new AdditionalViewModel(AdditionalViewType.DEFAULT, AdditionalInfo.empty());
    }

    public VersionedModelManager() {
        this(new AcademyDirectory(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAcademyDirectoryFilePath() {
        return userPrefs.getAcademyDirectoryFilePath();
    }

    @Override
    public void setAcademyDirectoryFilePath(Path academyDirectoryFilePath) {
        requireNonNull(academyDirectoryFilePath);
        userPrefs.setAcademyDirectoryFilePath(academyDirectoryFilePath);
    }

    //=========== AcademyDirectory ================================================================================

    @Override
    public void setAcademyDirectory(ReadOnlyAcademyDirectory academyDirectory) {
        this.academyDirectory.resetData(academyDirectory);
    }

    @Override
    public ReadOnlyAcademyDirectory getAcademyDirectory() {
        return academyDirectory;
    }

    @Override
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return academyDirectory.hasStudent(student);
    }

    @Override
    public void deleteStudent(Student target) {
        academyDirectory.removeStudent(target);
    }

    @Override
    public void addStudent(Student student) {
        academyDirectory.addStudent(student);
        updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
    }

    @Override
    public void setStudent(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);
        academyDirectory.setStudent(target, editedStudent);
    }

    //=========== Filtered Student List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Student} backed by the internal list of
     * {@code versionedAcademyDirectory}
     */
    @Override
    public ObservableList<Student> getFilteredStudentList() {
        return filteredStudents;
    }

    @Override
    public void updateFilteredStudentList(Predicate<Student> predicate) {
        requireNonNull(predicate);
        filteredStudents.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof VersionedModelManager)) {
            return false;
        }

        // state check
        VersionedModelManager other = (VersionedModelManager) obj;
        return academyDirectory.equals(other.academyDirectory)
                && userPrefs.equals(other.userPrefs)
                && filteredStudents.equals(other.filteredStudents);
    }

    //=========== Version Methods =========================================================================

    /**
     * Commits a particular command
     * @param message Message attached to the Commit for a readable explanation
     */
    @Override
    public void commit(String message) {
        versionControlController.commit(message);
    }

    @Override
    public Commit revert(String fiveCharHash) throws IOException {
        return versionControlController.revert(fiveCharHash);
    }

    @Override
    public StageArea getStageArea() {
        return versionControlController.getStageArea();
    }

    @Override
    public Commit getHeadCommit() {
        return versionControlController.getHeadCommit();
    }

    public Commit fetchCommitByLabel(String labelName) {
        return versionControlController.fetchCommitByLabel(labelName);
    }

    //=========== Additional Information View =============================================================

    /**
     * Set additional view type for shown in visualizer
     * @param additionalViewType additional view
     */
    @Override
    public void setAdditionalViewType(AdditionalViewType additionalViewType) {
        this.additionalViewModel.setAdditionalViewType(additionalViewType);
    }

    /**
     * Set Additional Info for visualizer
     * @param additionalInfo additional info
     */
    @Override
    public void setAdditionalInfo(AdditionalInfo<? extends Object> additionalInfo) {
        this.additionalViewModel.setAdditionalInfo(additionalInfo);
    }

    /**
     * Get Additional View Model
     * @return view model
     */
    @Override
    public AdditionalViewModel getAdditionalViewModel() {
        return this.additionalViewModel;
    }
}
