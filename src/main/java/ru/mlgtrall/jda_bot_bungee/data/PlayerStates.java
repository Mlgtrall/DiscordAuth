package ru.mlgtrall.jda_bot_bungee.data;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.ServersList;

public class PlayerStates {

    /**
     * Enum of states that defines current player's registration state.
     * <br/><br/>
     * <b>PLAYER_UNKNOWN</b> - Player is not in database, he is not permitted even to join.
     * <br/><br/>
     * <b>DISCORD_SYNCHRONIZATION</b> - Player is on discord sync state. He must perform <code>/authme &lt;code&gt;</code> command.
     * <br/><br/>
     * <b>REGISTRATION</b> - Player passed discord sync state and now he is on reg. state.
     * He must perform <code>/r &lt;password&gt; &lt;password&gt;</code> command.
     * <br/><br/>
     * <b>LOGIN</b> - Player passed registration and now he is on login state. He must perform
     * <code>/l &lt;password&gt;</code> command.
     * <br/><br/>
     * <b>VERIFIED</b> - Player is remembered by ip and permitted to join without third-party authorization.
     */
    enum AuthSessionState {
        PLAYER_UNKNOWN,
        DISCORD_SYNCHRONIZATION,
        REGISTRATION,
        LOGIN,
        VERIFIED;
    }

    enum SendToAddress {
        LOGIN_SERVER(ServersList.LOGIN),
        MAIN_SERVER(ServersList.MAIN),
        KICK(null);

        ServersList target;

        SendToAddress(ServersList server){
            this.target = server;
        }

        public ServersList getDestination() {
            return target;
        }
    }

    public static AuthSessionState defineState(@NotNull AuthPlayer p){
        if(!p.isInDatabase()){
            return AuthSessionState.PLAYER_UNKNOWN;
        }else if(p.getDiscordID() == null || p.getDiscordID().isEmpty()){
            return AuthSessionState.DISCORD_SYNCHRONIZATION;
        }else if(p.getHashedPassword() == null){
            return AuthSessionState.REGISTRATION;
        }else if(p.getLastIP() == null || p.getLastIP().isEmpty()){
            return AuthSessionState.LOGIN;
        }else{
            return AuthSessionState.VERIFIED;
        }
    }
}
