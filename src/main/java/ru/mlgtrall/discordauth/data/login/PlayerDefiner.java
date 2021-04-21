package ru.mlgtrall.discordauth.data.login;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.bootstrap.InjectorContainer;
import ru.mlgtrall.discordauth.data.AuthPlayer;
import ru.mlgtrall.discordauth.io.database.DataSource;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import ru.mlgtrall.discordauth.io.log.ConsoleLoggerFactory;

import java.net.SocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static ru.mlgtrall.discordauth.util.StringUtils.socketAddressToIp;

public class PlayerDefiner {

    private final LoginSession session;

    private final ConsoleLogger log = ConsoleLoggerFactory.get(this.getClass());

    private String playerName;
    private SocketAddress address;
    private final DataSource db;

    public PlayerDefiner(LoginSession session){
        this.session = session;
        this.db = Objects.requireNonNull(InjectorContainer.get().getSingleton(DataSource.class));
    }

    public PlayerDefiner(@NotNull LoginSession session, @NotNull DataSource db){
        this.session = session;
        this.db = db;
    }

    public PlayerDefiner byIp(@NotNull SocketAddress address){
        this.address = address;
        return this;
    }

    public PlayerDefiner byName(@NotNull String playerName){
        this.playerName = playerName;
        return this;
    }

    public LoginSession tryDefine(){
        log.debug("Defining player...");
        if(session.isPlayerDefined()){
            log.debug("Player already defined in this login session.");
            return session;
        }

        AuthPlayer player = null;

        //Defining player from global database with ip address
        if(address != null){
            log.debug("Defining by address...");
            List<AuthPlayer> players = null;

            players = db.getAllPlayersByIp(socketAddressToIp(address));

            if(players != null){
                //TODO: figure out how to better use it actually
                SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

                List<Date> dates = new ArrayList<>();

                for(AuthPlayer i : players){
                    String currentSDate = i.getLastDate();
                    if(currentSDate == null) continue;

                    Date currentDate;
                    try {
                        currentDate = format.parse(currentSDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        continue;
                    }
                    dates.add(currentDate);

                    Date currentMax = Collections.max(dates);
                    if(currentMax.equals(currentDate)){
                        player = i;
                    }
                }
            }


        }

        //Defining player from global database with his name
        if(player == null && playerName != null){
            player = db.getPlayer(playerName);
        }


        if(player != null){
            player.setInDatabase(true);
            log.debug("Player retrieved from db: " + player);
            session.setAuthPlayer(player);

            LoginState state = LoginState.defineState(player);
            session.setState(state);

            session.setPlayerDefined(true);
        }else{
            log.debug("Player retrieved from db: null");
            session.setPlayerDefined(false);
        }

        return session;
    }

}
