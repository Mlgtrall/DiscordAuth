package ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.mapper;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.Key;

public abstract class AbstractKeyMapper implements KeyMapper{

    protected final String separator;

    protected String path;

    protected final String keyName;

    protected String pathBeforeKey = "";

    protected String pathAfterKey = "";

    @Getter
    protected final Key key;

    public AbstractKeyMapper(@NotNull Key key, String separator){
        this.key = key;
        this.keyName = key.toString();
        this.separator =  separator;
        this.path = this.keyName;
    }

    @Override
    public KeyMapper addBefore(@NotNull String node){
        if(separator != null) {
            if (pathBeforeKey.isEmpty()) {
                pathBeforeKey += node + separator;
            } else {
                pathBeforeKey += separator + node + separator;
            }
            path = pathBeforeKey + keyName;
        }
        return this;
    }

    @Override
    public KeyMapper addAfter(@NotNull String node){
        if(separator != null) {
            if (pathAfterKey.isEmpty()) {
                pathAfterKey += node + separator;
            } else {
                pathAfterKey += separator + node + separator;
            }
            path = keyName + pathBeforeKey;
        }
        return this;
    }

    @Override
    public String getMapped(){
        return path;
    }

}
