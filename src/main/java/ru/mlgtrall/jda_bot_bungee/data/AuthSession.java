package ru.mlgtrall.jda_bot_bungee.data;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an authorization session of a connecting player.
 */

public final class AuthSession {

    private static final Map<SocketAddress, AuthSession> sessions = new HashMap<>();

    /**
     * This method must be called in ClientConnectEvent in order to initialize current session.
     *
     * @param address SocketAddress of the session
     * @return Returns new current session
     */
    public static @NotNull AuthSession init(SocketAddress address){
        AuthSession session = new AuthSession(address);
        sessions.put(address, session);
        return session;
    }

    @Getter
    private final SocketAddress socketAddress;
    @Getter @Setter
    private PendingConnection pendingConnection;
    @Getter @Setter
    private ProxiedPlayer proxiedPlayer;
    @Getter @Setter
    private ServerInfo target;

    private AuthSession(SocketAddress address){
        this.socketAddress = address;
    }

    public static AuthSession getSession(SocketAddress address){
        return sessions.get(address);
    }


    /**
     * This method must be called in ServerConnectedEvent in order to delete current AuthSession object.
     *
     * @param address SocketAddress of the session
     */
    public void close(SocketAddress address){
        sessions.put(address, null);
    }

    public void closeAll(){
        sessions.replaceAll((a, v) -> null);
    }


}
