package ru.mlgtrall.discordauth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.settings.Settings;
import ru.mlgtrall.discordauth.settings.holders.CoreSettings;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ServersList {

    @Inject
    private Settings settings;

    public static TargetServer WHITELIST;
    public static TargetServer LOGIN;

    private List<TargetServer> servers;

    public ServersList(){

    }

    @PostConstruct
    private void reload(){
        WHITELIST = new TargetServer(settings.getProperty(CoreSettings.BungeeCord.Registration.TARGET_SERVER));
        LOGIN = new TargetServer(settings.getProperty(CoreSettings.BungeeCord.Registration.LOGIN_SERVER));

        servers =  new ArrayList<>();
        servers.add(WHITELIST);
        servers.add(LOGIN);
    }

    public boolean isAuthorizedServer(@NotNull String name){
        for (TargetServer server : servers) {
            if(server.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public boolean isAuthorizedServer(@NotNull ServerInfo serverInfo){
        return isAuthorizedServer(serverInfo.getName());
    }

    public boolean isAuthorizedServer(@NotNull Server server){
        return isAuthorizedServer(server.getInfo().getName());
    }

    public boolean isLoginServer(String name){
        return LOGIN.getName().equals(name);
    }

    @ToString
    public static class TargetServer {

        @Getter
        private String name;

        @Getter
        private ServerInfo info;

        public TargetServer(String name){
            setName(name);
        }

        public void setName(String name) {
            this.name = name;
            setInfo(name);
        }

        private void setInfo(String name){
            this.info = ProxyServer.getInstance().getServerInfo(name);
        }
    }

}
