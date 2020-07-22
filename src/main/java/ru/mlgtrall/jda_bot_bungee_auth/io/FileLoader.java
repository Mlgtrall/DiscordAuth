package ru.mlgtrall.jda_bot_bungee_auth.io;

import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.Reloadable;
import ru.mlgtrall.jda_bot_bungee_auth.io.config.ConfigFile;
import ru.mlgtrall.jda_bot_bungee_auth.io.config.ConfigFileTemplate;
import ru.mlgtrall.jda_bot_bungee_auth.io.config.YMLConfigFile;
import ru.mlgtrall.jda_bot_bungee_auth.settings.Settings;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public final class FileLoader implements Reloadable {

    @Inject
    private Main pl;

    @Inject
    private Logger log;

    @Inject
    private TaskScheduler scheduler;

    @Inject
    private Settings settings;

    private Map<ConfigFileTemplate, ConfigFile> configFiles;

    public @NotNull ConfigFile getConfigFile(@NotNull ConfigFileTemplate file){
        return configFiles.get(file);
    }

    private void loadAllFiles(){
        log.info("Registering files...");
        configFiles = new ConcurrentHashMap<>();
        loadFromTemplates(
                ConfigFileTemplate.BUNGEE_CHAT,
                ConfigFileTemplate.DISCORD_CHAT,
                ConfigFileTemplate.PLUGIN_SETTINGS,
                ConfigFileTemplate.PLAYER_DB_YML
        );
        log.info("Registering files done!");
    }

    private void loadFromTemplates(ConfigFileTemplate... fileTemplates){
        if(fileTemplates == null) return;
        for(ConfigFileTemplate template : fileTemplates){
            if(template == null) { throw new NullPointerException("File template is null.");}
            if(template.getFile() != null){
                configFiles.put(template, new YMLConfigFile(template.getFile()));
            }
            else if(template.getFolderPath() != null){
                configFiles.put(template, new YMLConfigFile(template.getFileName(), template.getFolderPath()));
            }else if(template.getFileName() != null){

                configFiles.put(template, new YMLConfigFile(template.getFileName()));
            }else{
                log.info("Error in loading template " + template.toString() + " all params are null!");
            }
        }
    }

    public void reloadAllFiles(){
        for(ConfigFileTemplate file : ConfigFileTemplate.values()){
            configFiles.get(file).reload();
        }
    }

    @PostConstruct
    @Override
    public void reload() {
        loadAllFiles();
        //Periodically reloading files //TODO: remove to reload from command with file checking
        scheduler.schedule(pl, this::reloadAllFiles, 1, 30, TimeUnit.SECONDS); // May cause bug??
    }
}

