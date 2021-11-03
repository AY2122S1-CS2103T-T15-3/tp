package seedu.academydirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.academydirectory.commons.core.Config;
import seedu.academydirectory.commons.core.LogsCenter;
import seedu.academydirectory.commons.core.Version;
import seedu.academydirectory.commons.exceptions.DataConversionException;
import seedu.academydirectory.commons.util.ConfigUtil;
import seedu.academydirectory.commons.util.StringUtil;
import seedu.academydirectory.logic.Logic;
import seedu.academydirectory.logic.LogicManager;
import seedu.academydirectory.model.AcademyDirectory;
import seedu.academydirectory.model.ReadOnlyAcademyDirectory;
import seedu.academydirectory.model.ReadOnlyUserPrefs;
import seedu.academydirectory.model.UserPrefs;
import seedu.academydirectory.model.VersionedModel;
import seedu.academydirectory.model.VersionedModelManager;
import seedu.academydirectory.model.util.SampleDataUtil;
import seedu.academydirectory.storage.AcademyDirectoryStorage;
import seedu.academydirectory.storage.JsonAcademyDirectoryStorage;
import seedu.academydirectory.storage.JsonUserPrefsStorage;
import seedu.academydirectory.storage.Storage;
import seedu.academydirectory.storage.StorageManager;
import seedu.academydirectory.storage.UserPrefsStorage;
import seedu.academydirectory.ui.Ui;
import seedu.academydirectory.ui.UiManager;

/**
 * Runs the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(1, 3, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected VersionedModel model;
    protected Config config;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing AcademyDirectory ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        AcademyDirectoryStorage academyDirectoryStorage =
                new JsonAcademyDirectoryStorage(userPrefs.getAcademyDirectoryFilePath());
        storage = new StorageManager(academyDirectoryStorage, userPrefsStorage, userPrefs.getVersionControlPath());

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code VersionedModelManager} with the data from {@code storage}'s academy directory and
     * {@code userPrefs}. <br> The data from the sample academy directory will be used instead if {@code storage}'s
     * academy directory is not found, or an empty academy directory will be used instead if errors occur when reading
     * {@code storage}'s academy directory.
     */
    private VersionedModel initModelManager(Storage storage, ReadOnlyUserPrefs userPrefs) {
        Optional<ReadOnlyAcademyDirectory> academyDirectoryOptional;
        ReadOnlyAcademyDirectory initialData;
        try {
            academyDirectoryOptional = storage.readAcademyDirectory();
            if (academyDirectoryOptional.isEmpty()) {
                logger.info("Data file not found. Will be starting with a sample AcademyDirectory");
            }
            initialData = academyDirectoryOptional.orElseGet(SampleDataUtil::getSampleAcademyDirectory);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty AcademyDirectory");
            initialData = new AcademyDirectory();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty AcademyDirectory");
            initialData = new AcademyDirectory();
        }
        try {
            storage.saveAcademyDirectory(initialData);
        } catch (IOException e) {
            logger.warning("Unable to save to disk. Will not be able to revert properly..");
        }
        return new VersionedModelManager(initialData, userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
            logger.info(String.valueOf(initializedPrefs.getVersionControlPath()));
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty AcademyDirectory");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting AcademyDirectory " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Academy Directory ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }
}
