package ru.mlgtrall.discordauth.io.config;

import net.md_5.bungee.config.Configuration;
import ru.mlgtrall.discordauth.bootstrap.Reloadable;
import ru.mlgtrall.discordauth.bootstrap.Savable;

import java.io.File;


public interface ConfigFile extends Reloadable, Savable {
    Configuration getConfig();
    File getFile();
}
