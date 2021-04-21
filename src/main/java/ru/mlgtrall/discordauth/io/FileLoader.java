package ru.mlgtrall.discordauth.io;

import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.DiscordAuth;
import ru.mlgtrall.discordauth.bootstrap.Reloadable;
import ru.mlgtrall.discordauth.io.config.ConfigFile;
import ru.mlgtrall.discordauth.io.config.ConfigFileTemplates;
import ru.mlgtrall.discordauth.io.config.YMLConfigFile;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import ru.mlgtrall.discordauth.io.log.ConsoleLoggerFactory;
import ru.mlgtrall.discordauth.settings.Settings;
import ru.mlgtrall.discordauth.settings.holders.CoreSettings;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class FileLoader implements Reloadable {

    private static final ConsoleLogger log = ConsoleLoggerFactory.get(FileLoader.class);

    @Inject
    private DiscordAuth pl;

    @Inject
    private TaskScheduler scheduler;

    @Inject
    private Settings settings;

    private Map<String, ConfigFile> configFiles;

    private Map<String, File> files;

    public File getFile(String name){
        return files.get(name);
    }

    public @NotNull ConfigFile getConfigFile(@NotNull ConfigFileTemplates file){
        return configFiles.get(file.getFile().getName());
    }

    private void loadAll(){
        log.info("Registering files...");
        loadConfigs();
        //loadResources();
        log.info("Registering files done!");
    }

    private void loadConfigs(){
        configFiles = new ConcurrentHashMap<>();
        loadConfigsFromTemplates(
                ConfigFileTemplates.values()
        );
    }

    private void loadConfigsFromTemplates(ConfigFileTemplates... fileTemplates){
        if(fileTemplates == null || fileTemplates.length == 0) return;
        for(ConfigFileTemplates template : fileTemplates){
            if(template == null) { throw new NullPointerException("File template is null.");}

            if(template.getFile() != null){
                configFiles.put(template.getFile().getName(), new YMLConfigFile(template.getFile()));
            } else{
                log.exception(new IllegalStateException("Error in loading template \\\"\" + template.name() + \"\\\"!"));
            }
        }
    }

    private void reloadAll(){
        for(ConfigFileTemplates template : ConfigFileTemplates.values()){
            getConfigFile(template).reload();
        }
    }

    @PostConstruct
    @Override
    public void reload() {
        loadAll();
        //Periodically reloading files //TODO: remove to reload from command with file checking
        if(settings.getProperty(CoreSettings.WATCH_FILES)) {
            scheduler.schedule(pl, this::reloadAll, 1, 30, TimeUnit.SECONDS); // May cause bug??
        }
    }
}

