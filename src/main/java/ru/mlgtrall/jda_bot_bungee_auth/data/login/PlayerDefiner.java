package ru.mlgtrall.jda_bot_bungee_auth.data.login;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.InjectorContainer;
import ru.mlgtrall.jda_bot_bungee_auth.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.DataSource;
import ru.mlgtrall.jda_bot_bungee_auth.util.StringUtil;

import java.net.SocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import static ru.mlgtrall.jda_bot_bungee_auth.util.StringUtil.socketAddressToIp;

public class PlayerDefiner {

    private final LoginSession session;

    private final Logger log = Main.getInstance().getLogger();

    private String playerName;
    private SocketAddress address;
    private final DataSource source;

    public PlayerDefiner(LoginSession session){
        this.session = session;
        this.source = Objects.requireNonNull(InjectorContainer.get().getSingleton(DataSource.class));
    }

    public PlayerDefiner(@NotNull LoginSession session, @NotNull DataSource source){
        this.session = session;
        this.source = source;
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
        if(session.isPlayerDefined()) return session;

        AuthPlayer player = null;

        //Defining player from global database with ip address
        if(address != null){
            List<AuthPlayer> players = null;

            players = source.getAllPlayersByIp(socketAddressToIp(address));

            if(players != null){
                SimpleDateFormat format = new SimpleDateFormat();
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
            player = source.getPlayer(playerName);
        }


        if(player != null){
            log.info("Player retrieved from db: " + player);
            player.setInDatabase(true);
            session.setAuthPlayer(player);

            LoginState state = LoginState.defineState(player);
            session.setState(state);

            session.setPlayerDefined(true);
        }else{
            log.info("Player retrieved from db: null");
            session.setPlayerDefined(false);
        }

        return session;
    }

}
