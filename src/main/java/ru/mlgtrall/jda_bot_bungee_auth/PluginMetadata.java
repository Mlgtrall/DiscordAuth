package ru.mlgtrall.jda_bot_bungee_auth;

import net.md_5.bungee.config.Configuration;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.InjectorContainer;
import ru.mlgtrall.jda_bot_bungee_auth.io.FileLoader;
import ru.mlgtrall.jda_bot_bungee_auth.io.config.ConfigFile;
import ru.mlgtrall.jda_bot_bungee_auth.io.config.ConfigFileTemplate;

import java.io.File;
import java.io.FileReader;
import java.util.ResourceBundle;

public class PluginMetadata {

    private static final ConfigFile PLUGIN_CONF = InjectorContainer.get()
            .getSingleton(FileLoader.class)
            .getConfigFile(ConfigFileTemplate.PLUGIN_BUNGEE_YML);

    private static final String VERSION = PLUGIN_CONF
            .getConfig()
            .getString("version");

    private static final String PLUGIN_NAME = PLUGIN_CONF
            .getConfig()
            .getString("name");

    public static final String TOP_LVL_PACKAGE_NAME = PluginMetadata.class.getPackage().getName();


    public static String getVersion() {
        return VERSION;
    }

    public static String getPluginName() {
        return PLUGIN_NAME;
    }
}
