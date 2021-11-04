package seedu.academydirectory.logic;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.academydirectory.commons.core.GuiSettings;
import seedu.academydirectory.commons.core.LogsCenter;
import seedu.academydirectory.logic.commands.Command;
import seedu.academydirectory.logic.commands.CommandResult;
import seedu.academydirectory.logic.commands.exceptions.CommandException;
import seedu.academydirectory.logic.parser.AcademyDirectoryParser;
import seedu.academydirectory.logic.parser.exceptions.ParseException;
import seedu.academydirectory.model.AdditionalViewModel;
import seedu.academydirectory.model.ReadOnlyAcademyDirectory;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.student.Student;
import seedu.academydirectory.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_MESSAGE = "Could not save data to file: ";
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final VersionedModel model;
    private final Storage storage;
    private final AcademyDirectoryParser academyDirectoryParser;

    /**
     * Constructs a {@code LogicManager} with the given {@code VersionedModel} and {@code StorageManager}.
     */
    public LogicManager(VersionedModel model, Storage storage) {
        this.model = model;
        this.storage = storage;
        academyDirectoryParser = new AcademyDirectoryParser();
    }


    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        Command command = academyDirectoryParser.parseCommand(commandText);
        CommandResult commandResult = command.execute(model);

        try {
            storage.saveAcademyDirectory(model.getAcademyDirectory());
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        try {
            if (commandResult.getCommitMessage().isPresent()) {
                String commitMessage = commandResult.getCommitMessage().get();
                model.commit(commitMessage);
                storage.saveStageArea(model.getStageArea());
            }
        } catch (IOException ioe) {
            throw new CommandException(FILE_OPS_ERROR_MESSAGE + ioe, ioe);
        }

        return commandResult;
    }

    @Override
    public ReadOnlyAcademyDirectory getAcademyDirectory() {
        return model.getAcademyDirectory();
    }

    @Override
    public ObservableList<Student> getFilteredStudentList() {
        return model.getFilteredStudentList();
    }

    @Override
    public Path getAcademyDirectoryFilePath() {
        return model.getAcademyDirectoryFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }

    @Override
    public AdditionalViewModel getAdditionalViewModel() {
        return model.getAdditionalViewModel();
    }
}
