package ru.mlgtrall.jda_bot_bungee.io.database;

import org.jetbrains.annotations.NotNull;

public enum SQLKeys {
    DISCORD_ID("DISCORD_ID"), MINE_UUID("MINE_UUID"), REG_IP("REG_IP"),
    LAST_IP("LAST_IP"), VERIFIED("VERIFIED"), REG_DATE("REG_DATE"),
    LAST_DATE("LAST_DATE"), COUNTRY("COUNTRY"), MINE_NAME("MINE_NAME"),
    PASSWD_HASH("PASSWD_HASH"), SALT("SALT");

    String name;

    SQLKeys(String name){
        this.name = name;
    }

    @Override
    public @NotNull String toString() {
        return this.name;
    }

}