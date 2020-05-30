package ru.mlgtrall.jda_bot_bungee.io.database;

import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee.io.FileLoader;
import ru.mlgtrall.jda_bot_bungee.io.config.ConfigFile;
import ru.mlgtrall.jda_bot_bungee.io.config.ConfigFileTemplates;
import ru.mlgtrall.jda_bot_bungee.io.database.yml.YMLKeys;
import ru.mlgtrall.jda_bot_bungee.util.PlayerUtil;

import java.util.HashMap;
import java.util.Map;

public class YMLDatabase extends DatabaseProvider {

    private final FileLoader loader = FileLoader.getInstance();
    private final ConfigFile file = loader.getConfigFile(ConfigFileTemplates.PLAYER_DB_YML);
    private final Configuration config = file.getConfig();

    YMLDatabase(){}

    @Override
    public @Nullable AuthPlayer getPlayer(@NotNull String name){

        Configuration playerSection = config.getSection(name);

        //Checking if player exists
        if(playerSection == null) return null;

        Map<KeyHolder, String> playerData = new HashMap<>();
        playerData.put(YMLKeys.MINE_NAME, name);

        for(String key : playerSection.getKeys()) {
            for (KeyHolder valuableKey : YMLKeys.values()){
                if(key.equalsIgnoreCase(valuableKey.getName())) {
                    playerData.put(valuableKey, playerSection.getString(key));
                }
            }
        }
        return PlayerUtil.parse(PlayerUtil.convertYMLtoDataColumns(playerData));
    }


    @Override
    public @Nullable AuthPlayer getPlayer(@NotNull String key, @NotNull String data) {
        KeyHolder validKey = YMLKeys.isValidKey(key);
        if(validKey == null) return null;
        return getPlayer(validKey, data);
    }

    /**
     * Returns first player that was found with provided data associated with key.
     *
     * @param key the key that associated with data
     * @param data the data
     * @return first player type retrieved from config that was found with this key and data associated with key.
     */
    @Override
    public @Nullable AuthPlayer getPlayer(@NotNull KeyHolder key, @NotNull String data) {
        String found = null;

        for(String name : config.getKeys()){
            if(config.getString(name + "." + key).equals(data)) {
                found = name;
                break;
            }
        }

        if(found != null) return getPlayer(found);

        return null;
    }

//    @Override
//    public void newPlayer(@NotNull AuthPlayer player) {
//        String name = player.getName();
//        for(Map.Entry<KeyHolder, String> entry : PlayerUtil.convertDataColumnsToYML(PlayerUtil.parseToMap(player)).entrySet()){
//            config.set(name + "." + entry.getKey().getName(), entry.getValue());
//        }
//        file.save();
//    }

    //TODO: change return to boolean
    @Override
    public void set(@NotNull String key, @NotNull String data, @NotNull AuthPlayer player) {
        KeyHolder validKey = YMLKeys.isValidKey(key);
        if(validKey == null) return;
        set(validKey, data, player);
    }

    @Override
    public void set(@NotNull KeyHolder key, @NotNull String data, @NotNull AuthPlayer player) {
        String name = player.getName();
        config.set(name + "." + key.getName(), data);
        file.save();
    }

    @Override
    public void set(@NotNull AuthPlayer player) {
        String name = player.getName();
        for(Map.Entry<KeyHolder, String> entry : PlayerUtil.convertDataColumnsToYML(PlayerUtil.parseToMap(player)).entrySet()){
            config.set(name + "." + entry.getKey().getName(), entry.getValue());
        }
        file.save();
    }

    @Override
    public boolean containsPlayer(@NotNull AuthPlayer player) {
        return config.contains(player.getName());
    }

    @Override
    public boolean contains(@NotNull String key, @NotNull String data) {
        KeyHolder validKey = YMLKeys.isValidKey(key);
        if(validKey == null) return false;
        return contains(validKey, data);
    }

    @Override
    public boolean contains(@NotNull KeyHolder key, @NotNull String data) {
        for(String name : config.getKeys()){
            if(config.getString(name + "." + key.getName()).equals(data))
                return true;
        }
        return false;
    }

}
