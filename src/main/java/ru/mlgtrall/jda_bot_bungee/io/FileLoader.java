package ru.mlgtrall.jda_bot_bungee.io;

import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.io.config.ConfigFile;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class FileLoader {
    private static final Main pl = Main.getInstance();
    private static final Logger logger = pl.getLogger();
    private static final TaskScheduler scheduler = pl.getProxy().getScheduler();
    private static final FileLoader instance;


    static
    {
        instance = new FileLoader();
    }

    public static FileLoader getInstance() {
        return instance;
    }

    private static HashMap<ConfigFiles, ConfigFile> configFilesHashMap;

    public @NotNull
    ConfigFile get(@NotNull ConfigFiles file){
        return configFilesHashMap.get(file);
    }

    private FileLoader(){
        logger.info("Registering file loader");
        loadAllFiles();

        //Periodically reloading files
        scheduler.schedule(pl, FileLoader::reloadAllFiles, 1, 10, TimeUnit.SECONDS); // May cause bug??
        logger.info("Registering file loader done!");
    }

    private static void loadAllFiles(){
        configFilesHashMap = new HashMap<>();

        for(ConfigFiles file : ConfigFiles.values()){
            if(file == null) { throw new NullPointerException("file is null");}
            if(file.getFolderPath() != null){
                configFilesHashMap.put(file, new ConfigFile(file.getFileName(), file.getFolderPath()));
            }else if(file.getFileName() != null){
                configFilesHashMap.put(file, new ConfigFile(file.getFileName()));
            }else{
                logger.info("Error in loading file " + file.toString() + " all params are null!");
            }
        }

    }

    private static void reloadAllFiles(){

        for(ConfigFiles file : ConfigFiles.values()){
            configFilesHashMap.get(file).reload();
        }

    }

}

