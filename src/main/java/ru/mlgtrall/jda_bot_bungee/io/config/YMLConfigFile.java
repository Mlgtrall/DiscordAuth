package ru.mlgtrall.jda_bot_bungee.io.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.bootstrap.Reloadable;

import java.io.File;
import java.io.IOException;

public class YMLConfigFile implements ConfigFile, Reloadable {

    protected static final Main pl = Main.getInstance();

    private YMLConfigFile(){ }
    public YMLConfigFile(@NotNull String fileName){
        //this(fileName, pl.getDataFolder());
        this(new File(pl.getDataFolder() + File.separator + fileName));
    }

    public YMLConfigFile(@NotNull String fileName, @NotNull String folderPath){
        //this(fileName, new File(folderPath));
        this(new File(pl.getDataFolder() + File.separator + folderPath + File.separator + fileName));
    }

    public YMLConfigFile(@NotNull String fileName, @NotNull File folderPath){
        this(new File(folderPath.getPath() + File.separator + fileName));
    }

    public YMLConfigFile(@NotNull File file){
        this.file = file;
        this.dataFolder = pl.getDataFolder(); //TODO: поменять на проверку всего пути к файлу
        this.fileName = file.getName();
        load();
    }


    private File dataFolder;
    private File file;
    private Configuration config;
    private String fileName;



    private void checkDataFolder(){
        if(!dataFolder.exists()){
            pl.getLogger().info("No Data Folder found... Creating one...");
            if(dataFolder.mkdir()){
                pl.getLogger().info("making Data Folder ok!");
            }
            else{
                pl.getLogger().info("making Data Folder caused an error...");
            }
        }
    }

    private void checkIfFileExists() throws IOException {
        if (!file.exists()) {
            if (file.createNewFile()) {
                pl.getLogger().info("creating "+fileName+" ok!");
            } else {
                pl.getLogger().info("creating "+fileName+" caused an error!");
            }
        }
    }

    public void load(){
        checkDataFolder();

        try {
            checkIfFileExists();
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void save(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void reload(){
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public Configuration getConfig(){
        return config;
    }

    @Override
    public File getFile() {
        return file;
    }

}
