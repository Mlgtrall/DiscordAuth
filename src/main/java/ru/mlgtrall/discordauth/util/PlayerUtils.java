package ru.mlgtrall.discordauth.util;

import co.aikar.idb.DbRow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.discordauth.data.AuthPlayer;
import ru.mlgtrall.discordauth.io.database.keys.DataColumns;
import ru.mlgtrall.discordauth.io.database.keys.KeyHolder;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import ru.mlgtrall.discordauth.io.log.ConsoleLoggerFactory;
import ru.mlgtrall.discordauth.security.HashedPassword;

import java.util.*;


public final class PlayerUtils {

    private static ConsoleLogger log = ConsoleLoggerFactory.get(PlayerUtils.class);

    private PlayerUtils(){}

    public static @Nullable AuthPlayer newPlayerFromMap(@Nullable Map<KeyHolder, String> data){
        if(data==null) return null;
        nullOutNullStrings(data);
        return data.isEmpty() ? null :
                AuthPlayer.builder().name(data.get(DataColumns.MINE_NAME))
                .lastIP(data.get(DataColumns.LAST_IP))
                .lastDate(data.get(DataColumns.LAST_DATE))
                .country(data.get(DataColumns.COUNTRY))
                .hashedPassword(new HashedPassword(data.get(DataColumns.PASSWD_HASH), data.get(DataColumns.SALT)))
                .discordID(data.get(DataColumns.DISCORD_ID))
                .regDate(data.get(DataColumns.REG_DATE))
                .regIP(data.get(DataColumns.REG_IP))
                .uuid(Optional.ofNullable(data.get(DataColumns.MINE_UUID)).orElse("null").equals("null")  ? null : UUID.fromString(data.get(DataColumns.MINE_UUID)))
                .build();
    }

    public static @Nullable AuthPlayer newPlayerFromMap(@Nullable DbRow row){
        if(row == null || row.isEmpty()) return null;
        Map<KeyHolder, String> playerData = new HashMap<>();
        for(KeyHolder col : DataColumns.values()){
            playerData.put(col, row.getString(col.getContextualKey().toString()).isEmpty() ? null : row.getString(col.getContextualKey().toString()));
        }
        return playerData.isEmpty() ? null : newPlayerFromMap(playerData);
    }

    public static @Nullable List<AuthPlayer> newPlayersFromMaps(@Nullable List<DbRow> rows){
        if(rows == null || rows.isEmpty()) return null;
        List<AuthPlayer> players = new ArrayList<>();
        for(DbRow row : rows){
            players.add(newPlayerFromMap(row));
        }
        return players.isEmpty() ? null : players;
    }

    public static <K> Map<K, String> nullOutNullStrings(@NotNull Map<K, String> map){
        for (Map.Entry<K, String> entry: map.entrySet()) {
            if(entry.getValue().equalsIgnoreCase("null")){
                entry.setValue(null);
            }
        }
        return map;
    }

    public static boolean isEqual(DbRow row, @NotNull AuthPlayer player){
        AuthPlayer playerFromRow = newPlayerFromMap(row);
        return player.equals(playerFromRow);
    }


    public static @NotNull Map<KeyHolder, String> catchPlayerParsableData(@NotNull Map<String, String> map){
        Map<KeyHolder, String> newMap = new HashMap<>();
        for(Map.Entry<String, String> entry : map.entrySet()){
            for(DataColumns column : DataColumns.values()){
                if(entry.getKey().equals(column.getContextualKey().toString())){
                    newMap.put(column, entry.getValue());
                }
            }
        }
        return newMap;
    }

    //TODO: make it not linked to DataColumns order!!
    public static @NotNull Map<KeyHolder, String> parseToMap(@NotNull AuthPlayer p){
        Map<KeyHolder, String> map = new LinkedHashMap<>();
        map.put(DataColumns.DISCORD_ID, p.getDiscordID());
        map.put(DataColumns.MINE_UUID, Optional.ofNullable(p.getUuid()).isPresent() ? p.getUuid().toString() : null);
        map.put(DataColumns.REG_IP, p.getRegIP());
        map.put(DataColumns.LAST_IP, p.getLastIP());
        map.put(DataColumns.REG_DATE, p.getRegDate());
        map.put(DataColumns.LAST_DATE, p.getLastDate());
        map.put(DataColumns.COUNTRY, p.getCountry());
        map.put(DataColumns.MINE_NAME, p.getName());
        map.put(DataColumns.PASSWD_HASH, Optional.ofNullable(p.getHashedPassword()).isPresent() ? p.getHashedPassword().getHash() : null);
        map.put(DataColumns.SALT, Optional.ofNullable(p.getHashedPassword()).isPresent() ? p.getHashedPassword().getSalt() : null);

        return map;
    }

    //TODO: make it not linked to DataColumns order!!
    public static @NotNull Map<String, String> parseToRawMap(@NotNull AuthPlayer p){
        Map<String , String> map = new LinkedHashMap<>();
        map.put(DataColumns.DISCORD_ID.getContextualKey().toString(), p.getDiscordID());
        map.put(DataColumns.MINE_UUID.getContextualKey().toString(), Optional.ofNullable(p.getUuid()).isPresent() ? p.getUuid().toString() : null);
        map.put(DataColumns.REG_IP.getContextualKey().toString(), p.getRegIP());
        map.put(DataColumns.LAST_IP.getContextualKey().toString(), p.getLastIP());
        map.put(DataColumns.REG_DATE.getContextualKey().toString(), p.getRegDate());
        map.put(DataColumns.LAST_DATE.getContextualKey().toString(), p.getLastDate());
        map.put(DataColumns.COUNTRY.getContextualKey().toString(), p.getCountry());
        map.put(DataColumns.MINE_NAME.getContextualKey().toString(), p.getName());
        map.put(DataColumns.PASSWD_HASH.getContextualKey().toString(), Optional.ofNullable(p.getHashedPassword()).isPresent() ? p.getHashedPassword().getHash() : null);
        map.put(DataColumns.SALT.getContextualKey().toString(), Optional.ofNullable(p.getHashedPassword()).isPresent() ? p.getHashedPassword().getSalt() : null);
        return map;
    }


    public static void updateFromMap(@NotNull Map<KeyHolder, String> data, @NotNull AuthPlayer p){
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

    public boolean isDiscordLinked(@NotNull AuthPlayer pl){
        return pl.getDiscordID() != null && !pl.getDiscordID().isEmpty();
    }


}
