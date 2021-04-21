package ru.mlgtrall.discordauth.io.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.discordauth.bootstrap.Reloadable;
import ru.mlgtrall.discordauth.data.AuthPlayer;
import ru.mlgtrall.discordauth.io.database.keys.KeyHolder;

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
