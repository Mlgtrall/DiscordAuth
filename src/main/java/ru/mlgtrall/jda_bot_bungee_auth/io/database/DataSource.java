package ru.mlgtrall.jda_bot_bungee_auth.io.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.Reloadable;
import ru.mlgtrall.jda_bot_bungee_auth.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.KeyHolder;

import java.sql.SQLException;
import java.util.List;

//TODO: do more abstract for not only player entity + add async methods? do reloadable?
public interface DataSource extends Reloadable {

    default boolean isCached(){
        return false;
    }

    @Nullable AuthPlayer getPlayer(@NotNull String name);

    @Nullable List<AuthPlayer> getAllPlayers();

    @Nullable List<AuthPlayer> getAllPlayersByIp(@NotNull String ip);

    boolean savePlayer(@NotNull AuthPlayer authPlayer);

    //TODO: do save just data?

    boolean deletePlayer(@NotNull String name);

    boolean containsPlayer(@NotNull String playerName);

    boolean containsPlayer(@NotNull KeyHolder key, @NotNull AuthPlayer player);

    default void invalidateCache(String playerName){ }

    default void refreshCache(String playerName){ }
}
