package ru.mlgtrall.jda_bot_bungee_auth.io.database.keys;


import ru.mlgtrall.jda_bot_bungee_auth.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.DataSource;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.mapper.KeyMapper;

public interface KeyHolder{
    Key getContextualKey();
    Key getKey(Class<? extends DataSource> database);
    KeyMapper forPlayer(AuthPlayer player);
    String mysql();
    @Deprecated
    String yml();
}
