package ru.mlgtrall.jda_bot_bungee.io.database;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee.data.AuthPlayer;

public interface DataSource {

    @Nullable AuthPlayer getPlayer(@NotNull String name);

    @Nullable AuthPlayer getPlayer(@NotNull String key, @NotNull String data);

    @Nullable AuthPlayer getPlayer(@NotNull KeyHolder key, @NotNull String data);

//    void newPlayer(@NotNull AuthPlayer player);

    void set(@NotNull String key, @NotNull String data, @NotNull AuthPlayer player);

    void set(@NotNull KeyHolder key, @NotNull String data, @NotNull AuthPlayer player);

    void set(AuthPlayer player);

    boolean containsPlayer(@NotNull AuthPlayer player);

    boolean contains(@NotNull String key, @NotNull String data);

    boolean contains(@NotNull KeyHolder key, @NotNull String data);
}
