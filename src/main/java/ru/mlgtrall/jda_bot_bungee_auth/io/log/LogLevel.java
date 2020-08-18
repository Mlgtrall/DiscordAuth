package ru.mlgtrall.jda_bot_bungee_auth.io.log;

import org.jetbrains.annotations.NotNull;

public enum LogLevel {

    INFO(3),

    FINE(2),

    DEBUG(1);

    private int value;

    LogLevel(int value){
        this.value = value;
    }

    public boolean includes(@NotNull LogLevel level){
        return value <= level.value;
    }
}
