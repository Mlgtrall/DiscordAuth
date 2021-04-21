package ru.mlgtrall.discordauth.io.database;

import lombok.Getter;

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
