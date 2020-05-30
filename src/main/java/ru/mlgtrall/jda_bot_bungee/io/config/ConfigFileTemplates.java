package ru.mlgtrall.jda_bot_bungee.io.config;

import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;

import java.io.File;
import java.nio.file.Path;

@ToString
public enum ConfigFileTemplates { // TODO: refactor and move PathBuilder somewhere else
    BUNGEE_CHAT("config.yml"), JDA_CHAT("JDAconfig.yml"), PLAYER_DB_YML("players.yml"),
    SETTINGS("settings.yml");

    private final Main pl = Main.getInstance();

    /**
     * The File itself.
     */
    @Getter
    private final File file;

    /**
     * File's name with it's extension.
     */
    @Getter
    private final String fileName;

    /**
     * String pathway to file's folder with root from data folder.
     */
    @Getter
    private final String folderPath;



    ConfigFileTemplates(String fileName){
        this(fileName, null);
    }

    ConfigFileTemplates(String fileName, String folderPath){
        this.fileName = fileName;
        if(folderPath == null || !folderPath.trim().isEmpty()) {
            this.folderPath = pl.getDataFolder().getPath() + File.separator + folderPath;
        }else{
            this.folderPath = pl.getDataFolder().getPath();
        }
        file = new File(folderPath + File.separator + fileName);
    }

}
