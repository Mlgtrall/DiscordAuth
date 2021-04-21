package ru.mlgtrall.discordauth.bootstrap;

import ru.mlgtrall.discordauth.settings.Settings;

/**
 *  An extension of Reloadable class. Reloads or loads object with reloading its dependencies from settings.
 *
 *  TODO: make a reload command
 */
public interface SettingsDependent{

    /**
     * Performs a reload with the provided settings instance.
     *
     * @param settings the settings instance
     */
    void reload(Settings settings);
}
