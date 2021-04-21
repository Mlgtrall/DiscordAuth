package ru.mlgtrall.discordauth.message;

import lombok.Getter;
import ru.mlgtrall.discordauth.annotation.DataFolder;
import ru.mlgtrall.discordauth.bootstrap.Reloadable;
import ru.mlgtrall.discordauth.exception.NotExistingResourceException;
import ru.mlgtrall.discordauth.io.FileLoader;
import ru.mlgtrall.discordauth.io.config.ConfigFile;
import ru.mlgtrall.discordauth.io.config.ConfigFileTemplates;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import ru.mlgtrall.discordauth.io.log.ConsoleLoggerFactory;
import ru.mlgtrall.discordauth.settings.Settings;
import ru.mlgtrall.discordauth.settings.holders.CoreSettings;
import ru.mlgtrall.discordauth.util.FileUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;

import static ru.mlgtrall.discordauth.message.MessagePathHelper.DEFAULT_LANGUAGE;

public abstract class AbstractMessageFileHandler implements Reloadable {

    private final ConsoleLogger log = ConsoleLoggerFactory.get(AbstractMessageFileHandler.class);

    @DataFolder
    @Inject
    private File dataFolder;

    @Inject
    private FileLoader fileLoader;

    @Inject
    private Settings settings;

    @Getter
    private ConfigFile configFile;

    private final String defaultFile;

    protected AbstractMessageFileHandler(){
        this.defaultFile = createFilePath(DEFAULT_LANGUAGE);
    }

    @PostConstruct
    @Override
    public void reload() {
        final String language = getLanguage();
        final String filename = createFilePath(language);
        File messagesFile = initFile(filename);
        configFile = fileLoader.getConfigFile();//TODO:???????
    }

    protected String getLanguage(){
        return settings.getProperty(CoreSettings.Messages.GLOBAL_DEFAULT_LANGUAGE);
    }

    public String getMessage(String key){
        String message = configFile.getConfig().getString(key);
        return message == null
                ? "Error retrieving message '" + key + "'"
                : message;
    }

    public String getMessageIfExists(String key){
        return configFile.getConfig().getString(key);
    }

    /**
     * Creates the path to the messages file for the given language code.
     *
     * @param language the language code
     * @return path to the message file for the given language
     */
    protected abstract String createFilePath(String language);


    File initFile(String filePath){
        File file = new File(dataFolder, filePath);

        if(FileUtils.getResourceFromJar(filePath) != null) {
            try {
                return FileUtils.loadResourceFromJar(file, filePath);
            } catch (NotExistingResourceException e) {
                log.exception(e);
            }
        }
        return null;
    }


}
