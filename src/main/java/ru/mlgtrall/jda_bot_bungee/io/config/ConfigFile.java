package ru.mlgtrall.jda_bot_bungee.io.config;

import net.md_5.bungee.config.Configuration;
import ru.mlgtrall.jda_bot_bungee.bootstrap.Reloadable;
import ru.mlgtrall.jda_bot_bungee.bootstrap.Savable;

import java.io.File;


public interface ConfigFile extends Reloadable, Savable {
    Configuration getConfig();
    File getFile();

}
