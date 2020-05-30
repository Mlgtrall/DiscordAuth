package ru.mlgtrall.jda_bot_bungee.io;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.utils.CollectionUtils;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.io.config.ConfigFile;
import ru.mlgtrall.jda_bot_bungee.io.config.ConfigFileTemplates;
import ru.mlgtrall.jda_bot_bungee.io.config.YMLConfigFile;
import ru.mlgtrall.jda_bot_bungee.settings.Settings;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class FileLoader {

    private static final FileLoader instance;

    private static final Main pl = Main.getInstance();

    private static final Logger logger = pl.getLogger();

    private static final TaskScheduler scheduler = pl.getProxy().getScheduler();

    private static final SettingsManager settings = Settings.getInstance().getSettingsManager();


    static
    {
        instance = new FileLoader();
    }

//    public void addConfigFile(){
//
//    }

    public static FileLoader getInstance() {
        return instance;
    }

    private Map<ConfigFileTemplates, ConfigFile> configFiles;

    public @NotNull ConfigFile getConfigFile(@NotNull ConfigFileTemplates file){
        return configFiles.get(file);
    }

    private FileLoader(){
        logger.info("Registering file loader");
        loadAllFiles();

        //Periodically reloading files //TODO: remove to reload from command with file checking
        scheduler.schedule(pl, this::reloadAllFiles, 1, 10, TimeUnit.SECONDS); // May cause bug??
        logger.info("Registering file loader done!");
    }

    private void loadAllFiles(){
        configFiles = new HashMap<>();
        loadFromTemplates(
                ConfigFileTemplates.BUNGEE_CHAT,
                ConfigFileTemplates.JDA_CHAT,
                ConfigFileTemplates.SETTINGS,
                ConfigFileTemplates.PLAYER_DB_YML
        );

//        if(DatabaseProvider.currentDatabase().getClass() == YMLDatabase.class){
//            loadFromTemplates(ConfigFileTemplates.PLAYER_DB_YML);
//        }



    }

    private void loadFromTemplates(ConfigFileTemplates... fileTemplates){
        if(fileTemplates == null) return;
        for(ConfigFileTemplates file : fileTemplates){
            if(file == null) { throw new NullPointerException("file is null");}
            if(file.getFolderPath() != null){
                configFiles.put(file, new YMLConfigFile(file.getFileName(), file.getFolderPath()));
            }else if(file.getFileName() != null){

                configFiles.put(file, new YMLConfigFile(file.getFileName()));
            }else{
                logger.info("Error in loading file " + file.toString() + " all params are null!");
            }
        }
    }

    public void reloadAllFiles(){

        for(ConfigFileTemplates file : ConfigFileTemplates.values()){
            configFiles.get(file).reload();
        }

    }

}

