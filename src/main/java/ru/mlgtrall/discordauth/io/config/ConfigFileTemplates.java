package ru.mlgtrall.discordauth.io.config;

import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.DiscordAuth;
import ru.mlgtrall.discordauth.util.FileUtils;

import java.io.*;


/**
 * localFolderPath - folder where file will be contained (starting from .../plugin/)
 */
@ToString
public enum ConfigFileTemplates { // TODO: refactor and move PathBuilder somewhere else
    BUNGEE_CHAT("config.yml"),
    DISCORD_CHAT("JDAconfig.yml"),
    PLAYER_DB_YML("players.yml"),
    PLUGIN_SETTINGS("settings.yml"),
    PLUGIN_BUNGEE_YML("bungee.yml"){
        {
            this.file = FileUtils.getResourceAsFileFromJar("bungee.yml");
            this.shouldBeCopiedFromJar = false;
        }
    },
    MESSAGES_EN(null,"messages_en.yml", "messages/"),
    MESSAGES_RU(null,"messages_ru.yml", "messages/");

    protected final File dataFolder = DiscordAuth.getInstance().getDataFolder();

    /**
     * The File itself.
     */
    @Getter
    protected File file;

    @Getter
    protected ConfigFile configFile;

    @Getter
    protected String localResourcePath;

    protected boolean shouldBeCopiedFromJar = true;

    ConfigFileTemplates(@NotNull String fileName){
        this(null, fileName, null);
    }

    ConfigFileTemplates(String localFolderPath, @NotNull String fileName){
        this(localFolderPath, fileName, null);
    }

    ConfigFileTemplates(String localFolderPath, @NotNull String fileName, String localResourcePath){
        File folder;
        if(localFolderPath == null) {
            folder = dataFolder;
        }else{
            folder = new File(dataFolder + File.separator + localFolderPath);
        }
        this.file = new File(folder, fileName);

        if(localResourcePath == null){
            localResourcePath = fileName;
        }
        else if (localResourcePath.endsWith("/") || localResourcePath.endsWith("\\")){
            localResourcePath += fileName;
        }
        this.localResourcePath = localResourcePath;
    }

}
