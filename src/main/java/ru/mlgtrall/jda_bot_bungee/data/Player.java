package ru.mlgtrall.jda_bot_bungee.data;

import ru.mlgtrall.jda_bot_bungee.io.ConfigFiles;
import ru.mlgtrall.jda_bot_bungee.io.FileLoader;
import ru.mlgtrall.jda_bot_bungee.io.database.PlayerDatabase;

public class Player {
    String name;
    String hash;
    String salt;

    public Player(String name) throws Exception {
        if(PlayerDatabase.getPlayerData(FileLoader.getInstance().get(ConfigFiles.PLAYER_DB_YML).getConfig(), name) == null)
            throw new Exception("No such player in database");
        this.name = name;
    }




}
