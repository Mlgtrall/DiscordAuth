package ru.mlgtrall.jda_bot_bungee.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee.io.database.DataColumns;
import ru.mlgtrall.jda_bot_bungee.io.database.KeyHolder;
import ru.mlgtrall.jda_bot_bungee.io.database.yml.YMLKeys;
import ru.mlgtrall.jda_bot_bungee.security.HashedPassword;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ru.mlgtrall.jda_bot_bungee.io.database.DatabaseProvider.currentDatabase;

public final class PlayerUtil {

    private PlayerUtil(){}

    public static @NotNull AuthPlayer parse(@NotNull Map<KeyHolder, String> data){
        return AuthPlayer
                .builder()
                .name(data.get(DataColumns.MINE_NAME))
                .lastIP(data.get(DataColumns.LAST_IP))
                .lastDate(data.get(DataColumns.LAST_DATE))
                .country(data.get(DataColumns.COUNTRY))
                .hashedPassword(new HashedPassword(data.get(DataColumns.PASSWD_HASH), data.get(DataColumns.SALT)))
                .discordID(data.get(DataColumns.DISCORD_ID))
                .regDate(data.get(DataColumns.REG_DATE))
                .regIP(data.get(DataColumns.REG_IP))
                .uuid(UUID.fromString(data.get(DataColumns.MINE_UUID)))
                .build();
    }

    public static @NotNull Map<KeyHolder, String> convertDataColumnsToYML(@NotNull Map<KeyHolder, String> map){
        Map<KeyHolder, String> newMap = new HashMap<>();
        newMap.put(YMLKeys.MINE_NAME, map.get(DataColumns.MINE_NAME));
        newMap.put(YMLKeys.DISCORD_ID, map.get(DataColumns.DISCORD_ID));
        newMap.put(YMLKeys.REG_IP, map.get(DataColumns.REG_IP));
        newMap.put(YMLKeys.MINE_UUID, map.get(DataColumns.MINE_UUID));
        newMap.put(YMLKeys.COUNTRY, map.get(DataColumns.COUNTRY));
        newMap.put(YMLKeys.LOGIN_IP, map.get(DataColumns.LAST_IP));
        newMap.put(YMLKeys.PASSWD_HASH, map.get(DataColumns.PASSWD_HASH));
        newMap.put(YMLKeys.REG_DATE, map.get(DataColumns.REG_DATE));
        newMap.put(YMLKeys.SALT, map.get(DataColumns.SALT));
        return newMap;
    }

    public static @NotNull Map<KeyHolder, String> convertYMLtoDataColumns(@NotNull Map<KeyHolder, String> map){
        Map<KeyHolder, String> newMap = new HashMap<>();
        newMap.put(DataColumns.MINE_NAME, map.get(YMLKeys.MINE_NAME));
        newMap.put(DataColumns.DISCORD_ID, map.get(YMLKeys.DISCORD_ID));
        newMap.put(DataColumns.REG_IP, map.get(YMLKeys.REG_IP));
        newMap.put(DataColumns.MINE_UUID, map.get(YMLKeys.MINE_UUID));
        newMap.put(DataColumns.SALT, map.get(YMLKeys.SALT));
        newMap.put(DataColumns.REG_DATE, map.get(YMLKeys.REG_DATE));
        newMap.put(DataColumns.PASSWD_HASH, map.get(YMLKeys.PASSWD_HASH));
        newMap.put(DataColumns.LAST_IP, map.get(YMLKeys.LOGIN_IP));
        newMap.put(DataColumns.COUNTRY, map.get(YMLKeys.COUNTRY));
        return newMap;
    }

    public static @NotNull Map<KeyHolder, String> catchPlayerParsableData(@NotNull Map<String, String> map){
        Map<KeyHolder, String> newMap = new HashMap<>();
        for(Map.Entry<String, String> entry : map.entrySet()){
            for(DataColumns column : DataColumns.values()){
                if(entry.getKey().equals(column.getName())){
                    newMap.put(column, entry.getValue());
                }
            }
        }
        return newMap;
    }

    public static @NotNull Map<KeyHolder, String> parseToMap(@NotNull AuthPlayer p){
        Map<KeyHolder, String> map = new HashMap<>();
        map.put(DataColumns.COUNTRY, p.getCountry());
        map.put(DataColumns.DISCORD_ID, p.getDiscordID());
        map.put(DataColumns.LAST_DATE, p.getLastDate());
        map.put(DataColumns.LAST_IP, p.getLastIP());
        map.put(DataColumns.MINE_NAME, p.getName());
        map.put(DataColumns.MINE_UUID, p.getUuid().toString());
        map.put(DataColumns.PASSWD_HASH, p.getHashedPassword().getHash());
        map.put(DataColumns.SALT, p.getHashedPassword().getSalt());
        map.put(DataColumns.REG_DATE, p.getLastDate());
        map.put(DataColumns.REG_IP, p.getRegIP());
        return map;
    }

    public static void parseFromMap(@NotNull Map<KeyHolder, String> data, @NotNull AuthPlayer p){
        p.setName( data.get(DataColumns.MINE_NAME) );
        p.setLastIP( data.get(DataColumns.LAST_IP) );
        p.setDiscordID( data.get(DataColumns.DISCORD_ID) );
        p.setHashedPassword( new HashedPassword(data.get(DataColumns.PASSWD_HASH), data.get(DataColumns.SALT)) );
        p.setCountry( data.get(DataColumns.COUNTRY) );
        p.setLastDate( data.get(DataColumns.LAST_DATE) );
        p.setRegDate( data.get(DataColumns.REG_DATE) );
        p.setRegIP( data.get(DataColumns.REG_IP) );
        p.setUuid( UUID.fromString(data.get(DataColumns.MINE_UUID)) );
    }

    public static boolean checkIfInDatabase(AuthPlayer pl){
        return currentDatabase().containsPlayer(pl);
    }

    public boolean isDiscordLinked(@NotNull AuthPlayer pl){
        return pl.getDiscordID() != null && !pl.getDiscordID().isEmpty();
    }


}
