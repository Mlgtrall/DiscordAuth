package ru.mlgtrall.jda_bot_bungee_auth.settings;

import ch.jalu.configme.SettingsManagerImpl;
import ch.jalu.configme.configurationdata.ConfigurationData;
import ch.jalu.configme.migration.MigrationService;
import ch.jalu.configme.resource.PropertyResource;

import javax.inject.Inject;

public class Settings extends SettingsManagerImpl {

    @Inject
    public Settings(PropertyResource resource, ConfigurationData configurationData, MigrationService migrationService){
        super(resource, configurationData, migrationService);
    }

}
