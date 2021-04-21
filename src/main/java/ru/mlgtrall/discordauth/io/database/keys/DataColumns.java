package ru.mlgtrall.discordauth.io.database.keys;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.discordauth.bootstrap.InjectorContainer;
import ru.mlgtrall.discordauth.data.AuthPlayer;
import ru.mlgtrall.discordauth.io.database.CacheDataSource;
import ru.mlgtrall.discordauth.io.database.DataSource;
import ru.mlgtrall.discordauth.io.database.MySQLDatabase;
import ru.mlgtrall.discordauth.io.database.keys.mapper.KeyMapper;
import ru.mlgtrall.discordauth.io.database.keys.mapper.MySQLKeyMapper;

/*
    Preferred columns(keys to mapped data) for all databases
 */
public enum DataColumns implements KeyHolder {
    DISCORD_ID,
    MINE_UUID,
    REG_IP,
    LAST_IP("LAST_IP", "LOGIN_IP"),
    REG_DATE,
    LAST_DATE,
    COUNTRY,
    MINE_NAME,
    PASSWD_HASH,
    SALT;

    private final String mySQLKey;

    @Deprecated
    private final String ymlKey;

    DataColumns(){
        this.mySQLKey = this.name();
        this.ymlKey = this.name();
    }

    DataColumns(@NotNull String mySQLKey){
        this.mySQLKey = mySQLKey;
        this.ymlKey = mySQLKey;
    }

    DataColumns(@NotNull String mySQLKey, @NotNull String ymlKey){
        this.mySQLKey = mySQLKey;
        this.ymlKey = ymlKey;
    }

    @Override
    public @NotNull Key getContextualKey() {
        DataSource source = InjectorContainer.get().getSingleton(DataSource.class);
        if(source instanceof CacheDataSource){
            source = ((CacheDataSource) source).getSource();
        }
        return getKey(source.getClass());
    }

    @Override
    public @NotNull Key getKey(@NotNull Class<? extends DataSource> database) {
        if(database.equals(MySQLDatabase.class)) {
            return new MySQLKey(mySQLKey);
        }
        throw new IllegalArgumentException("Incorrect class parameter " + database.toString());
    }

    @Override
    public @NotNull KeyMapper forPlayer(AuthPlayer player) {
        return new MySQLKeyMapper(getContextualKey());
    }

    public static @Nullable KeyHolder isValidKey(String key) {
        for(KeyHolder val : DataColumns.values()){
            if(val.getContextualKey().toString().equalsIgnoreCase(key)) return val;
        }
        return null;
    }

    @Override
    public String mysql(){
        return mySQLKey;
    }

    @Override
    public String yml(){
        return ymlKey;
    }

    @Override
    public String toString() {
        return getContextualKey().toString();
    }
}
