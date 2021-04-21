package ru.mlgtrall.discordauth.io.config;

import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import static ru.mlgtrall.discordauth.util.FileUtils.*;

public class YMLConfigFile extends AbstractConfigFile {

    @Getter
    private Configuration config;

    public YMLConfigFile(@NotNull String fileName){
        super(fileName);
    }

    public YMLConfigFile(@NotNull String folderPath, @NotNull String fileName){
        super(folderPath, fileName);
    }

    public YMLConfigFile(@NotNull File file){
        super(file);
    }

    @Override
    protected void load(){
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
