package ru.mlgtrall.discordauth.settings.holders;

import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;

//TODO: add iterable to settings holders classes?
public final class SettingsRetriever {

    private SettingsRetriever(){}

    /**
     * Builds the configuration data for all property fields in plugin's {@link SettingsHolder} classes.
     *
     * @return configuration data
     */
    public static ConfigurationData buildGlobalConfigurationData(){
        return ConfigurationDataBuilder
                .createConfiguration(
                        DataSourceSettings.class,
                        DiscordSettings.class,
                        CoreSettings.class,
                        DataSourceSettings.MySQL.class,
                        DataSourceSettings.YML.class,
                        DiscordSettings.Guild.class,
                        DiscordSettings.Role.class,
                        CoreSettings.Messages.class,
                        CoreSettings.BungeeCord.class,
                        CoreSettings.BungeeCord.Registration.class,
                        CoreSettings.Debug.class,
                        CoreSettings.Debug.Lock.class,
                        CoreSettings.Security.class,
                        DiscordSettings.Channel.class,
                        DiscordSettings.Channel.Text.class,
                        DiscordSettings.Channel.Text.Listen.class
                );
    }
}
