package ru.mlgtrall.discordauth.io.database.keys;


import ru.mlgtrall.discordauth.data.AuthPlayer;
import ru.mlgtrall.discordauth.io.database.DataSource;
import ru.mlgtrall.discordauth.io.database.keys.mapper.KeyMapper;

public interface KeyHolder{
    Key getContextualKey();
    Key getKey(Class<? extends DataSource> database);
    KeyMapper forPlayer(AuthPlayer player);
    String mysql();
    @Deprecated
    String yml();
}
