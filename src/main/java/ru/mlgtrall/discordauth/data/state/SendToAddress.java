package ru.mlgtrall.discordauth.data.state;

import lombok.Getter;
import ru.mlgtrall.discordauth.Servers;

public enum SendToAddress {
    LOGIN_SERVER(Servers.LOGIN),
    TARGET_SERVER(Servers.WHITELIST),
    KICK(null);

    @Getter
    private final Servers target;

    SendToAddress(Servers server){
        this.target = server;
    }

}
