package ru.mlgtrall.jda_bot_bungee.io.file.config;

import net.md_5.bungee.config.Configuration;
import ru.mlgtrall.jda_bot_bungee.io.file.AbstractFile;


public interface ConfigFile extends AbstractFile {
    Configuration getConfig();

}
