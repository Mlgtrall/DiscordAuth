package ru.mlgtrall.jda_bot_bungee.io.config;

import net.md_5.bungee.config.Configuration;
import ru.mlgtrall.jda_bot_bungee.io.AbstractFile;


public interface ConfigFile extends AbstractFile {
    Configuration getConfig();

}
