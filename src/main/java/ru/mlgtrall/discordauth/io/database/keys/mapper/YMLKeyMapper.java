package ru.mlgtrall.discordauth.io.database.keys.mapper;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.io.database.keys.Key;

public class YMLKeyMapper extends AbstractKeyMapper {
    public YMLKeyMapper(@NotNull Key key) {
        super(key, ".");
    }
}
