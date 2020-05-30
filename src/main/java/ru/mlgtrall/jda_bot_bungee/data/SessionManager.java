package ru.mlgtrall.jda_bot_bungee.data;

import javafx.util.Pair;
import lombok.Getter;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.io.database.DataColumns;
import ru.mlgtrall.jda_bot_bungee.io.database.DatabaseProvider;

import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class SessionManager {

    private static final Main pl = Main.getInstance();

    private static final TaskScheduler scheduler = pl.getProxy().getScheduler();

    @Getter
    private static final Map<SocketAddress, AuthSession> pool = new ConcurrentHashMap<>(); //FIXME: Change to concurrent?

    private static final Map<AuthSession, Date> timeouts = new HashMap<>();

    /**
     * Default TIME_OUT tuple (Integer, TimeUnit).
     */
    private static final Pair<Integer, TimeUnit> TIME_OUT = new Pair<>(10, TimeUnit.MINUTES); //TODO: make configurable

    public static @NotNull AuthSession init(){
        AuthSession session = new AuthSession();
        pool.put(session.getSocketAddress(), session);
        setTimeout(session);
        return session;
    }


    private static void setTimeout(@NotNull AuthSession session){
        setTimeout(session.getSocketAddress());
    }

    /**
     * Sets the time out of the session to be deleted from memory after expiring.
     */
    private static void setTimeout(@NotNull SocketAddress address){
        scheduler.schedule(pl, () -> close(address), TIME_OUT.getKey(), TIME_OUT.getValue());
    }

    public static AuthSession getSession(SocketAddress address){
        return pool.get(address);
    }

    public static @NotNull List<AuthSession> getAliveSessions(){
        List<AuthSession> list = new ArrayList<>();
        for(Map.Entry<SocketAddress, AuthSession> entry : pool.entrySet()){
            list.add(entry.getValue());
        }
        return list;
    }

    /**
     * This method must be called in ServerConnectedEvent in order to delete current AuthSession object.
     *
     * @param address SocketAddress of the session
     */
    public static void close(@NotNull SocketAddress address){
        pool.remove(address);
    }

    public void closeAll(){
        pool.replaceAll((a, v) -> null);
    }
}
