package ru.mlgtrall.discordauth.io.database.keys;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.io.database.keys.mapper.AbstractKeyMapper;
import ru.mlgtrall.discordauth.io.database.keys.mapper.MySQLKeyMapper;

public class MySQLKey extends AbstractKey {
    public MySQLKey(@NotNull String name) {
        super(name);
    }

    @Override
    public AbstractKeyMapper mapper() {
        return new MySQLKeyMapper(this);
    }

}
