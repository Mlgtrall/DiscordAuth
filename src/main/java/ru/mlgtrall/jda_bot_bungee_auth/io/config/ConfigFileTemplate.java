package ru.mlgtrall.jda_bot_bungee_auth.io.config;

import lombok.Getter;
import lombok.ToString;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.annotation.DataFolder;

import javax.inject.Inject;
import java.io.File;

@ToString
public enum ConfigFileTemplate { // TODO: refactor and move PathBuilder somewhere else
    BUNGEE_CHAT("config.yml"), DISCORD_CHAT("JDAconfig.yml"), PLAYER_DB_YML("players.yml"),
    PLUGIN_SETTINGS("settings.yml");


    private static final File dataFolder = Main.getInstance().getDataFolder();

    /**
     * The File itself.
     */
    @Getter
    private File file;

    /**
     * File's name with it's extension.
     */
    @Getter
    private final String fileName;

    /**
     * String pathway to file's folder with root as data folder.
     */
    @Getter
    private String folderPath;

    ConfigFileTemplate(String fileName){
        this(fileName, null);
    }

    ConfigFileTemplate(String fileName, String folderPath){
        this.fileName = fileName;
    }

    static{
        for(ConfigFileTemplate that : ConfigFileTemplate.values()){

            if(that.folderPath != null && !that.folderPath.trim().isEmpty()) {

                that.folderPath = dataFolder.getAbsolutePath() + File.separator + that.folderPath;

            }else{
                that.folderPath = dataFolder.getAbsolutePath();
            }
            that.file = new File(that.folderPath + File.separator + that.fileName);
        }
    }


}
