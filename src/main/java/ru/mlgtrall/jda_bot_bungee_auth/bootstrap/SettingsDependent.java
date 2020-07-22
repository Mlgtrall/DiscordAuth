package ru.mlgtrall.jda_bot_bungee_auth.bootstrap;

import ru.mlgtrall.jda_bot_bungee_auth.settings.Settings;

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
