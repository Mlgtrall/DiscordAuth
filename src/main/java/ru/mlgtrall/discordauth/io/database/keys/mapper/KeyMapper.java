package ru.mlgtrall.discordauth.io.database.keys.mapper;

public interface KeyMapper {
    public KeyMapper addBefore(String node);
    public KeyMapper addAfter(String node);
    public String getMapped();
}
