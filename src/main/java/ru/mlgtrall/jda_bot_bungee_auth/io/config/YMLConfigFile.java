package ru.mlgtrall.jda_bot_bungee_auth.io.config;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.Main;

import java.io.File;
import java.io.IOException;

import static ru.mlgtrall.jda_bot_bungee_auth.util.FileUtil.*;

public class YMLConfigFile implements ConfigFile {

    private static final File dataFolder = Main.getInstance().getDataFolder();

    @Getter
    private final File file;

    @Getter
    private Configuration config;

    private final String fileName;

    public YMLConfigFile(@NotNull String fileName){
        this(new File(dataFolder + File.separator + fileName));
    }

    public YMLConfigFile(@NotNull String folderPath, @NotNull String fileName){
        this(new File(folderPath + File.separator + fileName));
    }

    public YMLConfigFile(@NotNull String fileName, @NotNull File folderPath){
        this(fileName, folderPath.getAbsolutePath());
    }

    public YMLConfigFile(@NotNull File file){
        this.file = file;
        this.fileName = file.getName();
        load();
    }



    public void load(){
        createDirIfNotExists(dataFolder);
        try {
            createFileIfNotExists(file);
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

}
