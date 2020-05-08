package ru.mlgtrall.jda_bot_bungee.io;

import ru.mlgtrall.jda_bot_bungee.Main;

import java.io.File;

public enum ConfigFiles {
    BUNGEE_CHAT("config.yml"), JDA_CHAT("JDAconfig.yml"), PLAYER_DB_YML("players.yml"),
    SETTINGS("settings.yml");

    private final String fileName;
    private final String folderPath;
    private final Main pl = Main.getInstance();

    String getFileName(){
        return this.fileName;
    }
    String getFolderPath(){
        return this.folderPath;
    }

    ConfigFiles(String fileName){
        this.fileName = fileName;
        this.folderPath = pl.getDataFolder().getPath();
    }
    ConfigFiles(String fileName, String folderPath){
        this.fileName = fileName;
        this.folderPath = folderPath;
    }
}
