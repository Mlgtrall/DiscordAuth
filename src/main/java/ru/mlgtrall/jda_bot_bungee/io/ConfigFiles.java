package ru.mlgtrall.jda_bot_bungee.io;

public enum ConfigFiles {
    BUNGEE_CHAT("config.yml"), JDA_CHAT("JDAconfig.yml"), PLAYER_DB_YML("players.yml"),
    SETTINGS("settings.yml");

    private final String fileName;
    private String folderPath;

    String getFileName(){
        return this.fileName;
    }
    String getFolderPath(){
        return this.folderPath;
    }

    ConfigFiles(String fileName){
        this.fileName = fileName;
    }
    ConfigFiles(String fileName, String folderPath){
        this.fileName = fileName;
        this.folderPath = folderPath;
    }
}
