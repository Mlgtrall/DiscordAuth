package ru.mlgtrall.jda_bot_bungee_auth.io.database.keys;

import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.mapper.AbstractKeyMapper;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.mapper.KeyMapper;

public interface Key {
    KeyMapper mapper();
}
