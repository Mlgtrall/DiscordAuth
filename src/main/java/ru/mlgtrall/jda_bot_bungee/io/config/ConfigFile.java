package ru.mlgtrall.jda_bot_bungee.io.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;

import java.io.File;
import java.io.IOException;

public class ConfigFile{
    private ConfigFile instance;

    private ConfigFile(){
        this.plugin = Main.getInstance();
    }
    public ConfigFile(@NotNull String fileName){
        this();
        this.fileName = fileName;
        this.dataFolder = plugin.getDataFolder();
        load();
    }

    public ConfigFile(@NotNull String fileName, String folderPath){
        this();
        this.fileName = fileName;
        if(folderPath == null){
            this.dataFolder = plugin.getDataFolder();
        }else {
            this.dataFolder = new File(plugin.getDataFolder().getPath() + File.separator + folderPath);
        }
        load();
    }

    protected final Main plugin;
    protected File dataFolder;
    protected File file;
    protected Configuration config;
    protected String fileName;

    public void checkDataFolder(){
        if(!dataFolder.exists()){
            plugin.getLogger().info("No Data Folder found... Creating one...");
            if(dataFolder.mkdir()){
                plugin.getLogger().info("making Data Folder ok!");
            }
            else{
                plugin.getLogger().info("making Data Folder caused an error...");
            }
        }

    }

    private void load(){
        checkDataFolder();


        file = new File(dataFolder + File.separator + fileName);
        try {
            if (!file.exists()) {
                if (file.createNewFile()) {
                    plugin.getLogger().info("creating "+fileName+" ok!");
                } else {
                    plugin.getLogger().info("creating "+fileName+" caused an error!");
                }
            }

            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void save(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void reload(){
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Configuration getConfig(){
        return config;
    }

}
