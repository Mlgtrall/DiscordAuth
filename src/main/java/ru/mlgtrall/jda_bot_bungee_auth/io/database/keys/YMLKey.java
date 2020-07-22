package ru.mlgtrall.jda_bot_bungee_auth.io.database.keys;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.mapper.AbstractKeyMapper;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.mapper.YMLKeyMapper;

public class YMLKey extends AbstractKey{

    public YMLKey(@NotNull String name) {
        super(name);
    }

    @Override
    public AbstractKeyMapper mapper() {
        return new YMLKeyMapper(this);
    }
}
