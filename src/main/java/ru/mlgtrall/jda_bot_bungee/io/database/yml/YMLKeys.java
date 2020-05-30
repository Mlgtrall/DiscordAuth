package ru.mlgtrall.jda_bot_bungee.io.database.yml;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee.io.database.DataColumns;
import ru.mlgtrall.jda_bot_bungee.io.database.KeyHolder;

@Deprecated
public enum YMLKeys implements KeyHolder {
    MINE_NAME("MINE_NAME"),
    DISCORD_ID("DISCORD_ID"),
    REG_IP("REG_IP"),
    MINE_UUID("MINE_UUID"),
    //LAST_DISPLAY_NAME("LAST_DISPLAY_NAME"),
    LOGIN_IP("LOGIN_IP"),
    REG_DATE("REG_DATE"),
    COUNTRY("COUNTRY"),
    SALT("SALT"),
    PASSWD_HASH("PASSWD_HASH");


    private final String name;
    private final String asPath;
    private String fullPath;
    public static final String defPathToPlayer = "";
    public static final String separator = ".";

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
    public @NotNull String getName() {
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

    public static @Nullable YMLKeys isValidKey(String key) {
        for(YMLKeys val : YMLKeys.values()){
            if(val.getName().equalsIgnoreCase(key)) return val;
        }
        return null;
    }
}

