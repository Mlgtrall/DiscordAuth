package ru.mlgtrall.jda_bot_bungee.io.config;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum YMLKeys {
    DISCORD_ID("DISCORD_ID"),
    PASSWORD("PASSWORD"),
    REG_IP("REG_IP"),
    MINE_UUID("MINE_UUID"),
    LAST_DISPLAY_NAME("LAST_DISPLAY_NAME"),
    LOGIN_IP("LOGIN_IP"),
    DISCORD_NICKNAME("DISCORD_NICKNAME"),
    DISCORD_ENAME("DISCORD_ENAME"),
    REG_DATE("REG_DATE"),
    COUNTRY("COUNTRY"),
    SALT("SALT"),
    PASSWD_HASH("PASSWD_HASH");

    private final String name;
    private final String asPath;
    private String fullPath;

    YMLKeys(){
        name = this.name();
        asPath = "." + name;
        fullPath = asPath;
    }
    YMLKeys(String name){
        this.name = name;
        asPath = "." + name;
        fullPath = asPath;
    }
    YMLKeys(String name, String path){
        this.name = name;
        this.asPath = path;
        this.fullPath = asPath;
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

    public @NotNull String getPath(){
        String result = fullPath;
        fullPath = asPath;
        return result;
    }

    public YMLKeys addBeforePath(String key){
        StringBuilder k = new StringBuilder(key);
        if(fullPath.startsWith(".")){
            if(!key.endsWith(".")){
                fullPath = key + fullPath;
            }else{
                fullPath = k.replace(k.lastIndexOf("."), k.lastIndexOf(".") + 1, "").toString() + fullPath;
            }
        }else {
            fullPath = key + "." + fullPath;
        }
        return this;
    }

    public @NotNull String getNameAsPath(){
        return this.asPath;
    }

    public @NotNull YMLKeys addToPath(String key){
        if(fullPath.endsWith(".")){
            fullPath += key;
        }else {
            fullPath += "." + key;
        }
        return this;
    }
}
