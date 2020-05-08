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

//    public void setInstance(FileLoader instance) {
//        FileLoader.instance = instance;
//    }

//    private ConfigFile bungeeChatConfig;
//    private ConfigFile jdaChatConfig;
//    private ConfigFile playerDataBase;
//    private ConfigFile settings;
//    private ConfigFile temp;

    private static HashMap<ConfigFiles, ConfigFile> configFilesHashMap;

    public @NotNull
    ConfigFile get(@NotNull ConfigFiles file){
        return configFilesHashMap.get(file);
    }

//    public ConfigFile getBungeeConfig() {
//        return bungeeChatConfig;
//    }
//
//    public ConfigFile getJDAConfig() {
//        return jdaChatConfig;
//    }
//
//    public ConfigFile getPlayerDataBase() {
//        return playerDataBase;
//    }
//
//    public ConfigFile getTemp(){
//        return temp;
//    }

    private FileLoader(){
        logger.info("Registering file loader");
        loadAllFiles();

        //Periodically loading files
        scheduler.schedule(pl, FileLoader::reloadAllFiles, 1, 10, TimeUnit.SECONDS); // May cause bug??
        logger.info("Registering file loader done!");
    }
//    public FileLoader(@NotNull Main plugin) {
//        //setInstance(this);
////        this.pl = plugin;
//        pl.getLogger().info("Registering file loader...");
//        logger = plugin.getLogger();
//        scheduler = plugin.getProxy().getScheduler();
//
//        loadAllFiles();
//
//        //Periodically loading files
//        scheduler.schedule(plugin, this::reloadAllFiles , 1, 10, TimeUnit.SECONDS); // May cause bug??
//        pl.getLogger().info("Registering file loader done!");
//    }


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
//        bungeeChatConfig = new YMLConfigFile("config.yml");
//        jdaChatConfig = new YMLConfigFile("JDAconfig.yml");
//        playerDataBase = new YMLConfigFile("players.yml");
//        settings = new YMLConfigFile("settings.yml");
        //temp = new YMLConfigFile("temp.yml");

        //new MySQL(playerDataBase.getConfig());

    }

    private static void reloadAllFiles(){
//        bungeeChatConfig.reload();
//        jdaChatConfig.reload();
//        playerDataBase.reload();
//        settings.reload();
//        temp.reload();
        for(ConfigFiles file : ConfigFiles.values()){
            configFilesHashMap.get(file).reload();
        }

    }

}

