package ru.mlgtrall.discordauth.data.state;

import lombok.Getter;
import lombok.ToString;
import ru.mlgtrall.discordauth.ServersList;


public enum SendToAddress {
    LOGIN_SERVER(ServersList.LOGIN),
    TARGET_SERVER(ServersList.WHITELIST),
    KICK(null);

    @Getter
    private final ServersList.TargetServer target;

    SendToAddress(ServersList.TargetServer server){
        this.target = server;
    }

}
