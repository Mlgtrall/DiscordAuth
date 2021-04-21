package ru.mlgtrall.discordauth.bootstrap;

import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.migration.MigrationService;
import ch.jalu.configme.migration.PlainMigrationService;
import ch.jalu.configme.resource.PropertyResource;
import ch.jalu.configme.resource.YamlFileResource;
import ru.mlgtrall.discordauth.annotation.DataFolder;
import ru.mlgtrall.discordauth.settings.Settings;
import ru.mlgtrall.discordauth.settings.holders.SettingsRetriever;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;

public class SettingsProvider implements Provider<Settings> {

    @Inject
    @DataFolder
    private File dataFolder;

    public SettingsProvider(){}

    @Override
    public Settings get() {
        File settingsFile = new File(dataFolder, "settings.yml");
        if(!settingsFile.exists()){
            try {
                Files.createFile(settingsFile.toPath(), (FileAttribute<?>) null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PropertyResource resource = new YamlFileResource(settingsFile);
        ConfigurationData configurationData = SettingsRetriever.buildGlobalConfigurationData();
        MigrationService migrationService = new PlainMigrationService();
        return new Settings(resource, configurationData, migrationService);
    }
}
