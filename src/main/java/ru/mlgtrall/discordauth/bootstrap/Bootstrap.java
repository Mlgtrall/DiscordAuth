package ru.mlgtrall.discordauth.bootstrap;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import co.aikar.commands.BungeeCommandManager;
import net.luckperms.api.LuckPerms;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.DiscordAuth;
import ru.mlgtrall.discordauth.PluginMetadata;
import ru.mlgtrall.discordauth.ServersList;
import ru.mlgtrall.discordauth.annotation.DataFolder;
import ru.mlgtrall.discordauth.annotation.LogFolder;
import ru.mlgtrall.discordauth.bootstrap.dependency.Dependencies;
import ru.mlgtrall.discordauth.bungee.command.*;
import ru.mlgtrall.discordauth.bungee.listener.PlayerListener;
import ru.mlgtrall.discordauth.data.login.LoginSessionPool;
import ru.mlgtrall.discordauth.io.FileLoader;
import ru.mlgtrall.discordauth.io.database.DataSource;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import ru.mlgtrall.discordauth.io.log.ConsoleLoggerFactory;
import ru.mlgtrall.discordauth.permission.PermissionConfigurator;
import ru.mlgtrall.discordauth.service.DiscordBotService;
import ru.mlgtrall.discordauth.settings.Settings;
import ru.mlgtrall.discordauth.settings.holders.CoreSettings;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public final class Bootstrap{

    //private static Bootstrap instance;

    private final DiscordAuth pl = DiscordAuth.getInstance();
    private ConsoleLogger log;

    private Injector injector;
    private TaskScheduler scheduler;
    private BungeeCommandManager commandManager;
    private Settings settings;
    private DataSource db;
    private DiscordBotService botService;

    public Bootstrap(){}

    public void start(){

        pl.getLogger().info("Starting initialization: ");

        configureInjector();

        if(!settings.getProperty(CoreSettings.ENABLED)){
            log.info("Stopping plugin because it is disabled in config...");

            pl.getProxy().stop("TargetServer stopped because discord auth plugin disabled in config!");

            log.fatal("TargetServer stopped because auth plugin disabled in config!",
                    new IllegalAccessException("TargetServer stopped because auth plugin disabled in config!"));
        }


        calculateDependencies();
        registerServices();
        registerCommands();
        registerListeners();

        log.info("Plugin loaded!");

    }

    private void configureInjector(){

        injector = new InjectorBuilder().addDefaultHandlers(PluginMetadata.TOP_LVL_PACKAGE_NAME).create();
        InjectorContainer.set(injector);
        injector.register(DiscordAuth.class, pl);
        injector.register(ProxyServer.class, pl.getProxy());
        injector.register(TaskScheduler.class, pl.getProxy().getScheduler());
        injector.register(Logger.class, pl.getLogger());
        injector.provide(DataFolder.class, pl.getDataFolder());

        //load logs
        Optional<File> logFolder = Optional.of(new File(pl.getDataFolder() + File.separator + "log"));
        logFolder.ifPresent(it -> injector.provide(LogFolder.class, it));
        //register injection providers
        injector.registerProvider(Settings.class, SettingsProvider.class);
        injector.registerProvider(DataSource.class, DataSourceProvider.class);

        loadGlobalLogger(logFolder.get());

        scheduler = pl.getProxy().getScheduler();
        settings = injector.getSingleton(Settings.class);
        if(settings == null){
            throw new IllegalStateException("Settings instance from injector can't be null.");
        }
        log.info("Settings loaded!");

        db = injector.getSingleton(DataSource.class);
        if(db == null){
            throw new IllegalStateException("DataSource instance from injector can't be null");
        }
        log.info("Data source loaded!");


        //Load instances
        botService = injector.getSingleton(DiscordBotService.class);
        injector.getSingleton(FileLoader.class);
        injector.getSingleton(LoginSessionPool.class);
        injector.getSingleton(ServersList.class);

    }

    private void registerServices(){
        log.info("Registering service instances...");
        injector.getSingleton(PermissionConfigurator.class);
        //TODO: Redo for multiple bots
        botService.startBot();
        log.info("Registering service instances done!");
    }



    private void calculateDependencies(){
        log.info("Loading dependencies...");
        //TODO: add throwable exception about dependencies' problems
        Dependencies.checkAll();
        injector.register(LuckPerms.class, Dependencies.LUCKY_PERMS.getAPI());
        log.info("Loading dependencies done!");
    }

    private void registerCommands(){
        log.info("Registering bungee commands...");
        PluginManager pluginManager = pl.getProxy().getPluginManager();

        commandManager = new BungeeCommandManager(pl);
        commandManager.enableUnstableAPI("help");
        injector.register(BungeeCommandManager.class, commandManager);
        //Load settings
        injector.getSingleton(ACFCommandManager.class);


    }

    private void registerListeners(){
        log.info("Registering bungee listeners...");
        PluginManager pluginManager = pl.getProxy().getPluginManager();

        pluginManager.registerListener(pl, injector.getSingleton(PlayerListener.class));
        log.info("Registering bungee listeners done!");
    }


    private void loadGlobalLogger(@NotNull File logFolder){
        logFolder.mkdirs();

        File globalLoggerFile = new File(logFolder, "global.log");
        try {
            globalLoggerFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConsoleLogger.init(pl.getLogger(), globalLoggerFile);
        log = ConsoleLoggerFactory.get(pl.getClass());
    }

/*  public static Bootstrap getInstance() {
        return instance == null ? instance = new Bootstrap() : instance;
    }
*/
}
