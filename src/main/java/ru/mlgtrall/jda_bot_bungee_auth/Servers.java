package ru.mlgtrall.jda_bot_bungee_auth;

import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.settings.Settings;

import javax.inject.Inject;

//TODO: make hook it bungee's config.yml?
public enum Servers {
    WHITELIST("whitelist_mcfp"), LOGIN("login");

    @Inject
    private static Settings settings;

    @Getter
    private final String name;

    @Getter
    private final ServerInfo info;

    Servers(String name){
        this.name = name;
        info = ProxyServer.getInstance().getServerInfo(name);
    }


    public static boolean isAuthorizedServer(@NotNull String name){
        for (Servers server : values()) {
            if(server.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public static boolean isAuthorizedServer(@NotNull ServerInfo serverInfo){
        return isAuthorizedServer(serverInfo.getName());
    }

    public static boolean isAuthorizedServer(@NotNull Server server){
        return isAuthorizedServer(server.getInfo().getName());
    }

    public static boolean isLoginServer(String name){
        return Servers.LOGIN.getName().equals(name);
    }



}
