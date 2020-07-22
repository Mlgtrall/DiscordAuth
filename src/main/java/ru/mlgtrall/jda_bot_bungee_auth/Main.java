package ru.mlgtrall.jda_bot_bungee_auth;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import ru.mlgtrall.jda_bot_bungee_auth.annotation.BungeeConfig;
import ru.mlgtrall.jda_bot_bungee_auth.annotation.DataFolder;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.InjectorContainer;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.SettingsProvider;
import ru.mlgtrall.jda_bot_bungee_auth.bungee.command.AuthCommand;
import ru.mlgtrall.jda_bot_bungee_auth.bungee.command.LoginCommand;
import ru.mlgtrall.jda_bot_bungee_auth.bungee.command.RegisterCommand;
import ru.mlgtrall.jda_bot_bungee_auth.bungee.listener.*;
import ru.mlgtrall.jda_bot_bungee_auth.data.login.LoginSessionPool;
import ru.mlgtrall.jda_bot_bungee_auth.data.login.LoginState;
import ru.mlgtrall.jda_bot_bungee_auth.io.FileLoader;
import ru.mlgtrall.jda_bot_bungee_auth.discord.DiscordBotService;
import net.md_5.bungee.api.plugin.Plugin;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.DataSource;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.DataSourceProvider;
import ru.mlgtrall.jda_bot_bungee_auth.settings.Settings;
import ru.mlgtrall.jda_bot_bungee_auth.settings.holders.CoreSettings;

import javax.annotation.CheckForNull;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;


public final class Main extends Plugin {

    private static Main instance;

    private Settings settings;

    private DataSource db;

    private DiscordBotService botService;

    private Injector injector;


    //TODO: move maps but i don't know where
    @Getter
    private HashMap<String,String> nameCodeMap;
    @Getter
    private HashMap<String,Integer> nameTaskIdMap;
    @Getter
    private HashMap<String,String> nameMineIdDiscordMap;

    @SneakyThrows
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

    private void build() throws Exception{
        getLogger().info("Initializing...");

        configureInjector();

        if(!settings.getProperty(CoreSettings.ENABLED)){
            getLogger().info("Stopping plugin because plugin disabled in config...");
            getProxy().stop("Server stopped because discord auth plugin disabled in config!");
            throw new IllegalAccessException("Server stopped because auth plugin disabled in config!");
        }

        nameTaskIdMap = new HashMap<>();
        nameCodeMap = new HashMap<>();
        nameMineIdDiscordMap = new HashMap<>();

        dependencies();
        registerServices();
        assembleBot();
        registerListeners();
        registerCommands();

        getLogger().info("All done!");
        getLogger().info("Plugin is fully operable!");
    }

    @SneakyThrows
    private void configureInjector(){

        //Load injector
        injector = new InjectorBuilder().addDefaultHandlers("ru.mlgtrall.jda_bot_bungee_auth").create();
        InjectorContainer.set(injector);
        injector.register(Main.class, this);
        injector.register(ProxyServer.class, this.getProxy());
        injector.register(TaskScheduler.class, this.getProxy().getScheduler());
        injector.register(Logger.class, this.getLogger());
        injector.provide(DataFolder.class, this.getDataFolder());
        injector.registerProvider(Settings.class, SettingsProvider.class);
        injector.registerProvider(DataSource.class, DataSourceProvider.class);

        settings = injector.getSingleton(Settings.class);
        if(settings == null){
            throw new IllegalStateException("Settings instance from injector can't be null.");
        }
        getLogger().info("Settings loaded!");

        db = injector.getSingleton(DataSource.class);
        if(db == null){
            throw new IllegalStateException("DataSource instance from injector can't be null");
        }
        getLogger().info("Data source loaded!");


        //Load instances
        botService = injector.getSingleton(DiscordBotService.class);
        injector.getSingleton(FileLoader.class);
        injector.getSingleton(LoginSessionPool.class);

    }

    private void dependencies() {
        getLogger().info("Loading dependencies...");
        if(getProxy().getPluginManager().getPlugin("LiteBans") == null){
            getLogger().warning("LiteBans plugin not found!");
        }else{
            getLogger().info("LiteBans plugin found. OK!");
        }
        getLogger().info("Loading dependencies done!");

    }

    public static void main(String[] args) {
    }

    private void registerListeners(){
        getLogger().info("Registering bungee listeners...");
        PluginManager pluginManager = getProxy().getPluginManager();

        pluginManager.registerListener(this, injector.getSingleton(PlayerListener.class));
        getLogger().info("Registering bungee listeners done!");
    }

    private void registerCommands(){
        getLogger().info("Registering bungee commands...");

        //Registering commands bungee
        getProxy().getPluginManager().registerCommand(this, injector.getSingleton(AuthCommand.class));
        getProxy().getPluginManager().registerCommand(this, injector.getSingleton(RegisterCommand.class));
        getProxy().getPluginManager().registerCommand(this, injector.getSingleton(LoginCommand.class));
        getLogger().info("Registering bungee commands done!");
    }

    private void registerServices() {
        getLogger().info("Registering service instances...");

        getLogger().info("Registering service instances done!");
    }

    public static Main getInstance() {
        return instance;
    }

    public static void setInstance(Main instance) {
        Main.instance = instance;
    }
}
