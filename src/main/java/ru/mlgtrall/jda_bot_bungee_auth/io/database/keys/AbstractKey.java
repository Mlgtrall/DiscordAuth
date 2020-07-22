package ru.mlgtrall.jda_bot_bungee_auth.io.database.keys;

import org.jetbrains.annotations.NotNull;

public abstract class AbstractKey implements Key {

    private final String name;

    public AbstractKey(@NotNull String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
