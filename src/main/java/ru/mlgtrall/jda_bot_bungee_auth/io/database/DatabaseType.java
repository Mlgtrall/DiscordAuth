package ru.mlgtrall.jda_bot_bungee_auth.io.database;

import lombok.Getter;

import java.util.Optional;

public enum DatabaseType { // TODO: use or not?
    @Deprecated
    YML("yml"),
    MY_SQL("mysql");

    @Getter
    private final String configName;

    DatabaseType(String configName){
        this.configName = configName;
    }
}
