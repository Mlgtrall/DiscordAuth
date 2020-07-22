package ru.mlgtrall.jda_bot_bungee_auth.bootstrap;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder;
import ch.jalu.configme.migration.MigrationService;
import ch.jalu.configme.migration.PlainMigrationService;
import ch.jalu.configme.resource.PropertyResource;
import ch.jalu.configme.resource.YamlFileResource;
import ru.mlgtrall.jda_bot_bungee_auth.annotation.DataFolder;
import ru.mlgtrall.jda_bot_bungee_auth.settings.Settings;
import ru.mlgtrall.jda_bot_bungee_auth.settings.holders.SettingsRetriever;
import ru.mlgtrall.jda_bot_bungee_auth.util.FileUtil;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

public class SettingsProvider implements Provider<Settings> {

    @Inject
    @DataFolder
    private File dataFolder;

    public SettingsProvider(){}

    @Override
    public Settings get() {
        File settingsFile = new File(dataFolder, "settings.yml");
        if(!settingsFile.exists()){
            FileUtil.create(settingsFile);
        }
        PropertyResource resource = new YamlFileResource(settingsFile);
        ConfigurationData configurationData = SettingsRetriever.buildConfigurationData();
        MigrationService migrationService = new PlainMigrationService();
        return new Settings(resource, configurationData, migrationService);
    }
}
