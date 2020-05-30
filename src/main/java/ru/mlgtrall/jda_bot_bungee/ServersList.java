package ru.mlgtrall.jda_bot_bungee;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

//TODO: make hook it bungee's config.yml
public enum ServersList {
    MAIN("whitelist_mcfp"), LOGIN("login");

    private static final Main pl = Main.getInstance();

    private final String name;

    private final ServerInfo serverInfo;


    public static boolean isAuthorizedServer(String name){
        for (ServersList server : values()) {
            if(server.getName().equals(name)){
                return true;
            }
        }
        return false;
    }
    public static boolean isLoginServer(String name){
        return ServersList.LOGIN.getName().equals(name);
    }

    public String getName() {
        return this.name;
    }
    ServersList(String name){
        this.name = name;
        serverInfo = ProxyServer.getInstance().getServerInfo(name);
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }
}
