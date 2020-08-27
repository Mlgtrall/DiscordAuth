package ru.mlgtrall.jda_bot_bungee_auth.io;

import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.Reloadable;
import ru.mlgtrall.jda_bot_bungee_auth.io.config.ConfigFile;
import ru.mlgtrall.jda_bot_bungee_auth.io.config.ConfigFileTemplate;
import ru.mlgtrall.jda_bot_bungee_auth.io.config.YMLConfigFile;
import ru.mlgtrall.jda_bot_bungee_auth.io.log.ConsoleLogger;
import ru.mlgtrall.jda_bot_bungee_auth.io.log.ConsoleLoggerFactory;
import ru.mlgtrall.jda_bot_bungee_auth.settings.Settings;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class FileLoader implements Reloadable {

    private static final ConsoleLogger log = ConsoleLoggerFactory.get(FileLoader.class);

    @Inject
    private Main pl;

    @Inject
    private TaskScheduler scheduler;

    @Inject
    private Settings settings;

    private Map<String, ConfigFile> configFiles;

    private Map<String, File> files;

    public File getFile(String name){
        return files.get(name);
    }

    public @NotNull ConfigFile getConfigFile(@NotNull ConfigFileTemplate file){
        return configFiles.get(file.getFileName());
    }

    private void loadAll(){
        log.info("Registering files...");
        loadConfigs();
        log.info("Registering files done!");
    }

    private void loadConfigs(){
        configFiles = new ConcurrentHashMap<>();
        loadConfigsFromTemplates(
                ConfigFileTemplate.values()
        );
    }


    private void loadConfigsFromTemplates(ConfigFileTemplate... fileTemplates){
        if(fileTemplates == null) return;
        for(ConfigFileTemplate template : fileTemplates){
            if(template == null) { throw new NullPointerException("File template is null.");}
            if(template.getFile() != null){
                configFiles.put(template.getFileName(), new YMLConfigFile(template.getFile()));
            }
            else if(template.getFolder() != null){
                configFiles.put(template.getFileName(), new YMLConfigFile(template.getFileName(), template.getFolder()));
            }else if(template.getFileName() != null){

                configFiles.put(template.getFileName(), new YMLConfigFile(template.getFileName()));
            }else{
                log.info("Error in loading template " + template.toString() + " all params are null!");
            }
        }
    }

    private void reloadAll(){
        for(ConfigFileTemplate template : ConfigFileTemplate.values()){
            configFiles.get(template.getFileName()).reload();
        }
    }

    @PostConstruct
    @Override
    public void reload() {
        loadAll();
        //Periodically reloading files //TODO: remove to reload from command with file checking
        scheduler.schedule(pl, this::reloadAll, 1, 30, TimeUnit.SECONDS); // May cause bug??
    }
}

