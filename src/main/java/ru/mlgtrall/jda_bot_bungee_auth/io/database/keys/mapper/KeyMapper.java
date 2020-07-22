package ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.mapper;

public interface KeyMapper {
    public KeyMapper addBefore(String node);
    public KeyMapper addAfter(String node);
    public String getMapped();
}
