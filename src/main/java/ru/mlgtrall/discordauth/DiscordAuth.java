package ru.mlgtrall.discordauth;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import ru.mlgtrall.discordauth.bootstrap.Bootstrap;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import java.util.concurrent.ConcurrentHashMap;


public final class DiscordAuth extends Plugin {

    private static DiscordAuth instance;

    //TODO: move maps but i don't know where yet
    @Getter
    private ConcurrentHashMap<String,String> nameCodeMap;
    @Getter
    private ConcurrentHashMap<String,Integer> nameTaskIdMap;
    @Getter
    private ConcurrentHashMap<String,String> nameMineIdDiscordMap;

    @Getter
    private Bootstrap bootstrap;

    /**
     * Required constructor to load plugin.
     */
    public DiscordAuth(){}

    @Override
    public void onEnable() {
        if(getInstance() != null){
            throw new IllegalStateException("Plugin already initialized!");
        }

        setInstance(this);
        getLogger().info("Plugin enabled! | by Mlgtrall");

        build();

    }

    //TODO: add some protection?
    @Override
    public void onDisable() {

        ConsoleLogger.closeFileWriter();
        getLogger().info("plugin disabled! | By Mlgtrall");
    }

    private void build(){

        nameTaskIdMap = new ConcurrentHashMap<>();
        nameCodeMap = new ConcurrentHashMap<>();
        nameMineIdDiscordMap = new ConcurrentHashMap<>();

        bootstrap = new Bootstrap();
        bootstrap.start();

    }

    public static void main(String[] args) {
    }



    public static DiscordAuth getInstance() {
        return instance;
    }

    public static void setInstance(DiscordAuth instance) {
        DiscordAuth.instance = instance;
    }
}
