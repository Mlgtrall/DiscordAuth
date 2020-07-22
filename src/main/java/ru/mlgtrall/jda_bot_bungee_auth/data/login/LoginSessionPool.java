package ru.mlgtrall.jda_bot_bungee_auth.data.login;

import com.google.common.collect.Multimap;
import lombok.Getter;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.DataSource;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.KeyHolder;
import ru.mlgtrall.jda_bot_bungee_auth.util.Pair;

import javax.inject.Inject;
import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static ru.mlgtrall.jda_bot_bungee_auth.data.login.LoginState.defineState;

public class LoginSessionPool {

    @Inject
    private Main pl;

    @Inject
    private DataSource db;

    @Inject
    private TaskScheduler scheduler;

    @Getter
    private final Map<SocketAddress, LoginSession> pool = new ConcurrentHashMap<>();

    @Getter
    private final Map<LoginSession, Date> timeouts = new ConcurrentHashMap<>();


    /**
     * Default TIME_OUT tuple (Integer, TimeUnit).
     */
    //TODO: jar don't see javafx.util.Pair
    private final Pair<Integer, TimeUnit> DEFAULT_TIME_OUT = new Pair<>(10, TimeUnit.MINUTES); //TODO: make configurable

    public LoginSessionPool(){}

    //TODO: change return type to LoginSession.Builder
    public @NotNull LoginSession initSession(SocketAddress address){
        LoginSession session = LoginSession.builder().build();
        pool.put(address, session);
        setTimeout(address);
        return session;
    }


    /**
     * Sets the time out of the session to be deleted from memory after expiring.
     */
    private void setTimeout(@NotNull SocketAddress address){
        scheduler.schedule(pl, () -> close(address), DEFAULT_TIME_OUT.getKey(), DEFAULT_TIME_OUT.getValue());
    }

    public LoginSession getSession(SocketAddress address){
        return pool.get(address);
    }

    public @NotNull List<LoginSession> getAliveSessions(){
        List<LoginSession> list = new ArrayList<>();
        for(Map.Entry<SocketAddress, LoginSession> entry : pool.entrySet()){
            list.add(entry.getValue());
        }
        return list;
    }

    /**
     * This method must be called in ServerConnectedEvent in order to delete current LoginSession object.
     *
     * @param address SocketAddress of the session
     */
    public void close(@NotNull SocketAddress address){
        pool.remove(address);
    }

    public void closeAll(){
        pool.replaceAll((a, v) -> null);
    }
}
