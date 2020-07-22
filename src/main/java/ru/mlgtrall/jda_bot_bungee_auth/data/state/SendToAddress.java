package ru.mlgtrall.jda_bot_bungee_auth.data.state;

import lombok.Getter;
import ru.mlgtrall.jda_bot_bungee_auth.Servers;

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
