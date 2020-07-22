package ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.mapper;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.Key;

public class YMLKeyMapper extends AbstractKeyMapper {
    public YMLKeyMapper(@NotNull Key key) {
        super(key, ".");
    }
}
