package ru.mlgtrall.jda_bot_bungee.io.database;

import org.jetbrains.annotations.Nullable;

/*
    Preferred columns(keys to mapped data) for all databases
 */
public enum DataColumns implements KeyHolder {
    DISCORD_ID("DISCORD_ID"),
    MINE_UUID("MINE_UUID"),
    REG_IP("REG_IP"),
    LAST_IP("LAST_IP", "LOGIN_IP"),
    REG_DATE("REG_DATE"),
    LAST_DATE("LAST_DATE"),
    COUNTRY("COUNTRY"),
    MINE_NAME("MINE_NAME"),
    PASSWD_HASH("PASSWD_HASH"),
    SALT("SALT");


    private final String mySql;

    private final String yml;

    DataColumns(){
        this.mySql = this.name();
        this.yml = this.name();
    }

    DataColumns(String mySql){
        this.mySql = mySql;
        this.yml = mySql;
    }

    DataColumns(String mySql, String yml){
        this.mySql = mySql;
        this.yml = yml;
    }

    @Override
    public String getName() {
        if(DatabaseProvider.currentDatabase().getClass() == YMLDatabase.class){
            return yml;
        }else{
            return mySql;
        }
    }

    public static @Nullable DataColumns isValidKey(String key) {
        for(DataColumns val : DataColumns.values()){
            if(val.getName().equalsIgnoreCase(key)) return val;
        }
        return null;
    }
}
