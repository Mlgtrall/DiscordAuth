package ru.mlgtrall.jda_bot_bungee_auth.io.database.migration;

import co.aikar.idb.DB;
import co.aikar.idb.Database;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.InjectorContainer;
import ru.mlgtrall.jda_bot_bungee_auth.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.DataSource;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.DataColumns;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.KeyHolder;
import ru.mlgtrall.jda_bot_bungee_auth.util.PlayerUtil;

import java.io.File;
import java.sql.Connection;
import java.util.*;

public class YMLToMySQL {

    private static final Main pl = Main.getInstance();
    private static final TaskScheduler scheduler = pl.getProxy().getScheduler();

    public static void performMigrationAll(@NotNull Configuration config){
        DataSource db = InjectorContainer.get().getSingleton(DataSource.class);
        List<AuthPlayer> players = getAllPlayers(config);
        for (AuthPlayer player : players){
            scheduler.runAsync(pl,() -> db.savePlayer(player));
        }
    }

    public static @Nullable AuthPlayer getPlayer(@NotNull Configuration config, @NotNull String name){
        config = config.getSection(name);
        if(config == null) return null;
        Map<KeyHolder, String> playerData = new HashMap<>();
        for(DataColumns key : DataColumns.values()){
            if(DataColumns.MINE_NAME.equals(key)){
                playerData.put(key, name);
                continue;
            }
            String value = config.getString(key.yml());
            playerData.put(key, value);
        }
        return PlayerUtil.newPlayerFromMap(playerData);
    }

    public static @NotNull List<AuthPlayer> getAllPlayers(@NotNull Configuration config){
        Collection<String> names = config.getKeys();
        List<AuthPlayer> players = new ArrayList<>();
        for (String name : names){
            players.add(getPlayer(config, name));
        }
        return players;
    }

}
