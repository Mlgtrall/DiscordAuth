package ru.mlgtrall.discordauth.io.database.migration;

import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.discordauth.DiscordAuth;
import ru.mlgtrall.discordauth.bootstrap.InjectorContainer;
import ru.mlgtrall.discordauth.data.AuthPlayer;
import ru.mlgtrall.discordauth.io.database.DataSource;
import ru.mlgtrall.discordauth.io.database.keys.DataColumns;
import ru.mlgtrall.discordauth.io.database.keys.KeyHolder;
import ru.mlgtrall.discordauth.util.PlayerUtils;

import java.util.*;

public class YMLToMySQL {

    private static final DiscordAuth pl = DiscordAuth.getInstance();
    private static final TaskScheduler scheduler = pl.getProxy().getScheduler();

    public static void performMigrationAll(@NotNull Configuration config){
        DataSource db = InjectorContainer.get().getSingleton(DataSource.class);
        List<AuthPlayer> players = getAllPlayers(config);
        for (AuthPlayer player : players){
            if(db.getPlayer(player.getName()) == null)
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
        return PlayerUtils.newPlayerFromMap(playerData);
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
