package ru.mlgtrall.discordauth.io.config;

import lombok.Getter;
import ru.mlgtrall.discordauth.DiscordAuth;
import ru.mlgtrall.discordauth.bootstrap.Reloadable;

import java.io.File;

public abstract class AbstractConfigFile implements ConfigFile, Reloadable {

    protected static final File dataFolder = DiscordAuth.getInstance().getDataFolder();

    @Getter
    protected final File file;

    protected AbstractConfigFile(String fileName){
        this(new File(dataFolder + File.separator + fileName));
    }

    protected AbstractConfigFile(String folderPath, String fileName){
        this(new File(folderPath + File.separator + fileName));
    }

    protected AbstractConfigFile(File file){
        this.file = file;
        load();
    }

    protected abstract void load();
}
