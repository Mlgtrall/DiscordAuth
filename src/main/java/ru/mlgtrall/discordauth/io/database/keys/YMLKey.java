package ru.mlgtrall.discordauth.io.database.keys;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.io.database.keys.mapper.AbstractKeyMapper;
import ru.mlgtrall.discordauth.io.database.keys.mapper.YMLKeyMapper;

public class YMLKey extends AbstractKey{

    public YMLKey(@NotNull String name) {
        super(name);
    }

    @Override
    public AbstractKeyMapper mapper() {
        return new YMLKeyMapper(this);
    }
}
