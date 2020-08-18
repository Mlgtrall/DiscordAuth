package ru.mlgtrall.jda_bot_bungee_auth;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import co.aikar.commands.BungeeCommandManager;
import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import ru.mlgtrall.jda_bot_bungee_auth.annotation.DataFolder;
import ru.mlgtrall.jda_bot_bungee_auth.annotation.LogFolder;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.InjectorContainer;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.SettingsProvider;
import ru.mlgtrall.jda_bot_bungee_auth.bungee.command.AuthCommand;
import ru.mlgtrall.jda_bot_bungee_auth.bungee.command.BungeeCommands;
import ru.mlgtrall.jda_bot_bungee_auth.bungee.command.LoginCommand;
import ru.mlgtrall.jda_bot_bungee_auth.bungee.command.RegisterCommand;
import ru.mlgtrall.jda_bot_bungee_auth.bungee.listener.*;
import ru.mlgtrall.jda_bot_bungee_auth.data.login.LoginSessionPool;
import ru.mlgtrall.jda_bot_bungee_auth.io.FileLoader;
import ru.mlgtrall.jda_bot_bungee_auth.discord.DiscordBotService;
import net.md_5.bungee.api.plugin.Plugin;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.DataSource;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.DataSourceProvider;
import ru.mlgtrall.jda_bot_bungee_auth.io.log.ConsoleLogger;
import ru.mlgtrall.jda_bot_bungee_auth.io.log.ConsoleLoggerFactory;
import ru.mlgtrall.jda_bot_bungee_auth.settings.Settings;
import ru.mlgtrall.jda_bot_bungee_auth.settings.holders.CoreSettings;
import ru.mlgtrall.jda_bot_bungee_auth.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Logger;


public final class Main extends Plugin {

    private static Main instance;

    private Settings settings;

    private DataSource db;

    private DiscordBotService botService;

    private Injector injector;

    private ConsoleLogger log;

    @Getter
    private BungeeCommandManager commandManager;

    private TaskScheduler scheduler;


    //TODO: move maps but i don't know where
    @Getter
    private HashMap<String,String> nameCodeMap;
    @Getter
    private HashMap<String,Integer> nameTaskIdMap;
    @Getter
    private HashMap<String,String> nameMineIdDiscordMap;

    /**
     * Required constructor to load plugin.
     */
    public Main(){}

    @Override
    public void onEnable() {
        setInstance(this);
        getLogger().info("Plugin enabled! | by Mlgtrall");

        build();

    }
    
    private void assembleBot(){
        //Loading bot
        botService.startBot();
    }

    @Override
    public void onDisable() {
        getLogger().info("plugin disabled! | By Mlgtrall");
    }

    private void build(){
        getLogger().info("Initializing...");

        configureInjector();

        if(!settings.getProperty(CoreSettings.ENABLED)){
            log.info("Stopping plugin because plugin disabled in config...");
            getProxy().stop("Server stopped because discord auth plugin disabled in config!");
            log.fatal("Server stopped because auth plugin disabled in config!", new IllegalAccessException("Server stopped because auth plugin disabled in config!"));
        }

        nameTaskIdMap = new HashMap<>();
        nameCodeMap = new HashMap<>();
        nameMineIdDiscordMap = new HashMap<>();

        dependencies();
        registerServices();
        assembleBot();
        registerListeners();
        registerCommands();

        log.info("All done!");
        log.info("Plugin is fully operable!");
    }

    private void configureInjector(){

        //Load injector
        injector = new InjectorBuilder().addDefaultHandlers(PluginMetadata.TOP_LVL_PACKAGE_NAME).create();
        InjectorContainer.set(injector);
        injector.register(Main.class, this);
        injector.register(ProxyServer.class, this.getProxy());
        injector.register(TaskScheduler.class, this.getProxy().getScheduler());
        injector.register(Logger.class, this.getLogger());
        injector.provide(DataFolder.class, this.getDataFolder());
        Optional<File> logFolder;
        try {
            logFolder = Optional.of(new File(this.getDataFolder().getCanonicalPath() + File.separator + "log"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to load log folder.");
        }
        logFolder.ifPresent(it -> injector.provide(LogFolder.class, it));
        injector.registerProvider(Settings.class, SettingsProvider.class);
        injector.registerProvider(DataSource.class, DataSourceProvider.class);

        loadLogger(logFolder.get());

        scheduler = getProxy().getScheduler();
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

    }

    private void loadLogger(File logFolder){
        FileUtil.checkOrCreateDirQuietly(logFolder);
        File globalLoggerFile = new File(logFolder, "global.log");
        FileUtil.checkOrCreateFileQuietly(globalLoggerFile);
        ConsoleLogger.init(getLogger(), globalLoggerFile);
        log = ConsoleLoggerFactory.get(this.getClass());
    }

    private void dependencies() {
        log.info("Loading dependencies...");
        if(getProxy().getPluginManager().getPlugin("LiteBans") == null){
            log.warning("LiteBans plugin not found!");
        }else{
            log.info("LiteBans plugin found. OK!");
        }
        log.info("Loading dependencies done!");

    }

    public static void main(String[] args) {
    }

    private void registerListeners(){
        log.info("Registering bungee listeners...");
        PluginManager pluginManager = getProxy().getPluginManager();

        pluginManager.registerListener(this, injector.getSingleton(PlayerListener.class));
        log.info("Registering bungee listeners done!");
    }

    private void registerCommands(){
        log.info("Registering bungee commands...");
        PluginManager pluginManager = getProxy().getPluginManager();

        commandManager = new BungeeCommandManager(this);

        commandManager.registerCommand(injector.getSingleton(BungeeCommands.ChangePasswordCommand.class));
        //commandManager.registerCommand(injector.getSingleton(BungeeCommands.PluginCommands.class));



        //LuckPerms perms = LuckPermsProvider.get();

        //Registering commands bungee
        pluginManager.registerCommand(this, injector.getSingleton(AuthCommand.class));
        pluginManager.registerCommand(this, injector.getSingleton(RegisterCommand.class));
        pluginManager.registerCommand(this, injector.getSingleton(LoginCommand.class));
        log.info("Registering bungee commands done!");
    }

    private void registerServices() {
        log.info("Registering service instances...");

        log.info("Registering service instances done!");
    }

    public static Main getInstance() {
        return instance;
    }

    public static void setInstance(Main instance) {
        Main.instance = instance;
    }
}
