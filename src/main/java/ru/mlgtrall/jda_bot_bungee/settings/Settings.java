package ru.mlgtrall.jda_bot_bungee.settings;

import ch.jalu.configme.SettingsManager;
import ch.jalu.configme.SettingsManagerBuilder;
import ch.jalu.configme.SettingsManagerImpl;
import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import ch.jalu.configme.migration.MigrationService;
import ch.jalu.configme.resource.PropertyResource;
import ch.jalu.configme.resource.YamlFileResource;
import lombok.Getter;
import lombok.Singular;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee.io.config.ConfigFileTemplates;
import ru.mlgtrall.jda_bot_bungee.settings.holders.DataSourceSettingsHolder;
import ru.mlgtrall.jda_bot_bungee.settings.holders.DiscordSettingsHolder;
import ru.mlgtrall.jda_bot_bungee.settings.holders.SystemSettingsHolder;

//TODO: extends SettingsManagerImpl
public class Settings{

    private static Settings instance;

    @Getter
    private final ConfigurationData configurationData;
    @Getter
    private final SettingsManager settingsManager;

    public static Settings getInstance() {
        return instance == null ? new Settings() : instance;
    }

    private Settings(){
        //super(resource, cd, ms);
        configurationData = ConfigurationDataBuilder
                .createConfiguration(
                        DataSourceSettingsHolder.class, DiscordSettingsHolder.class, SystemSettingsHolder.class,
                        DataSourceSettingsHolder.MySQL.class, DataSourceSettingsHolder.YML.class, DiscordSettingsHolder.Guild.class,
                        DiscordSettingsHolder.Role.class, SystemSettingsHolder.BungeeCord.class, SystemSettingsHolder.Debug.class
                );

        settingsManager = SettingsManagerBuilder.withYamlFile(ConfigFileTemplates.SETTINGS.getFile())
                .configurationData(configurationData).useDefaultMigrationService().create();

    }

}
