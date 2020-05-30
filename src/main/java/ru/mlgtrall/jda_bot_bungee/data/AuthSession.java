package ru.mlgtrall.jda_bot_bungee.data;

import lombok.*;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.io.database.DataColumns;
import ru.mlgtrall.jda_bot_bungee.io.database.DatabaseProvider;

import java.net.SocketAddress;

/**
 * This class represents an authorization session of a connecting player.
 * Session class help to interact with player's data while session exists.
 */

@Data
public final class AuthSession {

//    int sessionId;

    private SocketAddress socketAddress;

    private PendingConnection pendingConnection;

    private ProxiedPlayer proxiedPlayer;

    private PlayerStates.SendToAddress target;

    private AuthPlayer authPlayer;

    private boolean playerDefined = false;

    public AuthSession(@NotNull PendingConnection pendingConnection) {
        this.pendingConnection = pendingConnection;
        this.socketAddress = pendingConnection.getVirtualHost();
    }

    public void close(){
        SessionManager.getPool().remove(socketAddress);
    }

    public static @NotNull AuthSession.AuthSessionBuilder builder(){
        return new AuthSessionBuilder(new AuthSession());
    }

    public static @NotNull AuthSession.AuthSessionBuilder toBuilder(AuthSession session){
        return new AuthSessionBuilder(session);
    }

    @Contract(value = " -> new", pure = true)
    public @NotNull AuthSessionBuilder toBuilder(){
        return new AuthSessionBuilder(this);
    }

    //TODO: can i move it to lombok?
    public static class AuthSessionBuilder {

        private final AuthSession session;

        AuthSessionBuilder(AuthSession session){
            this.session = session;
        }
        /**
         * Defines player from provided information that is required to seek it in database.
         *
         * @param col data column in database
         * @param data the data itself in column
         * @return true if
         */
        private boolean definePlayer(@NotNull DataColumns col, @NotNull String data){
            //TODO: if in database -> seek what state and what to do
            if(session.isPlayerDefined()) return true;

            //Check player in database
            AuthPlayer p = DatabaseProvider.currentDatabase().getPlayer(col.getName(), data);

            //If player null => player is not in database => false
            if(p == null) {
                session.setPlayerDefined(false);
                return false;
            }
            session.setAuthPlayer( p );

            session.setPlayerDefined(true);
            session.getAuthPlayer().setInDatabase(true);
            p.setSessionState( PlayerStates.defineState(p) );

            return true;
        }

        public AuthSessionBuilder pendingConnection(@NotNull PendingConnection p){
            if (!definePlayer(DataColumns.LAST_IP, p.getSocketAddress().toString())) {
                definePlayer(DataColumns.REG_IP, p.getSocketAddress().toString());
            }
            session.setSocketAddress(p.getVirtualHost());
            session.setPendingConnection(p);
            return this;
        }

        public AuthSessionBuilder proxiedPlayer(@NotNull ProxiedPlayer p){
            definePlayer(DataColumns.MINE_NAME, p.getName());
            session.setProxiedPlayer(p);
            return this;
        }

        public AuthSession build(){
            return session;
        }

    }
}
