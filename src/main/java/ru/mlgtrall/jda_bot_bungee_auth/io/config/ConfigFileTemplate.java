package ru.mlgtrall.jda_bot_bungee_auth.io.config;

import com.google.common.collect.Streams;
import com.google.common.io.Files;
import lombok.Getter;
import lombok.ToString;
import net.dv8tion.jda.core.utils.IOUtil;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.util.FileUtil;

import java.io.*;

@ToString
public enum ConfigFileTemplate { // TODO: refactor and move PathBuilder somewhere else
    BUNGEE_CHAT("config.yml"),
    DISCORD_CHAT("JDAconfig.yml"),
    PLAYER_DB_YML("players.yml"),
    PLUGIN_SETTINGS("settings.yml"),
    PLUGIN_BUNGEE_YML("bungee.yml"){
        {
            this.file = FileUtil.getResourceAsFile(Main.getInstance(), "bungee.yml");
        }
    };



    protected final File dataFolder = Main.getInstance().getDataFolder();

    /**
     * The File itself.
     */
    @Getter
    protected File file;

    /**
     * File's name with it's extension.
     */
    @Getter
    protected String fileName;


    /**
     * String pathway to file's folder with root as data folder.
     */
    @Getter
    protected File folder;

    ConfigFileTemplate(@NotNull String fileName){
        this(fileName, null);
    }

    ConfigFileTemplate(@NotNull String fileName, String folderPath){
        this.fileName = fileName;
        if(folderPath == null){
            this.folder = dataFolder;
        }else {
            this.folder = new File(folderPath);
        }

        this.file = new File(folder, fileName);
    }

//    static{
//        for(ConfigFileTemplate that : ConfigFileTemplate.values()){
//
//            if(that.folderPath != null && !that.folderPath.trim().isEmpty()) {
//
//                that.folderPath = dataFolder.getAbsolutePath() + File.separator + that.folderPath;
//
//            }else{
//                that.folderPath = dataFolder.getAbsolutePath();
//            }
//            that.file = new File(that.folderPath + File.separator + that.fileName);
//        }
//    }


}
