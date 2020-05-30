package ru.mlgtrall.jda_bot_bungee.io.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee.io.database.mysql.MySQLDriver;
import ru.mlgtrall.jda_bot_bungee.util.PlayerUtil;

import java.util.Map;

public class MySQLDatabase extends DatabaseProvider {

    MySQLDriver mysql = MySQLDriver.getInstance();

    MySQLDatabase(){}

    @Override
    public AuthPlayer getPlayer(@NotNull String name) {
        return getPlayer(DataColumns.MINE_NAME, name);
    }

    @Override
    public @Nullable AuthPlayer getPlayer(@NotNull String key, @NotNull String data) {
        KeyHolder validKey = DataColumns.isValidKey(key);
        if(validKey == null){
            return null;
        }
        return getPlayer(validKey, data);
    }

    @Override
    public @Nullable AuthPlayer getPlayer(@NotNull KeyHolder key, @NotNull String data) {
        Map<String,String> map = mysql.queryRow(key.getName(), data);
        if(map == null){
            return null;
        }
        Map<KeyHolder, String> playerDataMap = PlayerUtil.catchPlayerParsableData(map);
        return PlayerUtil.parse(playerDataMap);
    }

//    @Override
//    public void newPlayer(@NotNull AuthPlayer player) {
//        mysql.insert(PlayerUtil.parseToMap(player));
//    }


    //FIXME: Should be called asynchronously in login session?
    @Override
    public void set(@NotNull KeyHolder key, @NotNull String data, @NotNull AuthPlayer player) {
        mysql.update(key.getName(), data, DataColumns.MINE_NAME.getName(), player.getName());
    }

    @Override
    public void set(@NotNull AuthPlayer player) {
        if(!containsPlayer(player)) {
            mysql.insert(PlayerUtil.parseToMap(player));
        }
        for(Map.Entry<KeyHolder, String> entry : PlayerUtil.parseToMap(player).entrySet()){
            mysql.update(entry.getKey().getName(), entry.getValue(), DataColumns.MINE_NAME.getName(), player.getName());
        }
    }

    @Override
    public void set(@NotNull String key, @NotNull String data,  @NotNull AuthPlayer player){
        KeyHolder validKey = DataColumns.isValidKey(key);
        if(validKey == null) return;
        set(validKey, data, player);
    }

    @Override
    public boolean containsPlayer(@NotNull AuthPlayer player) {
        return contains(DataColumns.MINE_NAME, player.getName());
    }

    @Override
    public boolean contains(@NotNull String key, @NotNull String data) {
        KeyHolder validKey = DataColumns.isValidKey(key);
        if(validKey == null) return false;
        return contains(validKey, data);
    }

    @Override
    public boolean contains(@NotNull KeyHolder key, @NotNull String data) {
        return mysql.containsRow(key.getName(), data);
    }

}
