package ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.mapper;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.Key;

public class MySQLKeyMapper extends AbstractKeyMapper {
    public MySQLKeyMapper(@NotNull Key key) {
        super(key, null);
    }

    @Override
    public KeyMapper addBefore(@NotNull String node) {
        return this;
    }

    @Override
    public KeyMapper addAfter(@NotNull String node) {
        return this;
    }
}
