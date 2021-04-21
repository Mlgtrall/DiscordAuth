package ru.mlgtrall.discordauth.data.login;

import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.discordauth.data.AuthPlayer;
import ru.mlgtrall.discordauth.data.state.SendToAddress;
import ru.mlgtrall.discordauth.util.BungeeChatConfig;

/**
 * Enum of states that defines current player's registration state.
 * <br/><br/>
 * <b>PLAYER_UNKNOWN</b> - Player is not in database, he is not permitted even to join.
 * <br/><br/>
 * <b>DISCORD_SYNCHRONIZATION</b> - Player is on discord sync state. He must perform <code>/authme &lt;code&gt;</code> command.
 * <br/><br/>
 * <b>REGISTRATION</b> - Player passed discord sync state and now he is on reg. State.
 * He must perform <code>/r &lt;password&gt; &lt;password&gt;</code> command.
 * <br/><br/>
 * <b>LOGIN</b> - Player passed registration and now he is on login state. He must perform
 * <code>/l &lt;password&gt;</code> command.
 * <br/><br/>
 * <b>VERIFIED</b> - Player is remembered by ip and permitted to join without third-party authorization.
 */
public enum LoginState {
    //TODO: refactor a bit for reaching more modular pattern (if I want to do some stages optional)
    PLAYER_UNKNOWN{
        @Override
        public @NonNull TextComponent getKickReason() {
            return BungeeChatConfig.fromConfig("not_in_whitelist");
        }
        @Override
        public SendToAddress getSendTo() {
            return SendToAddress.KICK;
        }
    },
    NOT_IN_GUILD{
        @Override
        public @NonNull TextComponent getKickReason() {
            return BungeeChatConfig.fromConfig("quit_discord");
        }

        @Override
        public SendToAddress getSendTo() {
            return SendToAddress.KICK;
        }
    },
    UNKNOWN_STATE{
        @Override
        public @NonNull TextComponent getKickReason() {
            return BungeeChatConfig.fromConfig("unknown_reason");
        }

        @Override
        public SendToAddress getSendTo() {
            return SendToAddress.KICK;
        }
    },
    NO_ROLE{
        @Override
        public @NonNull TextComponent getKickReason() {
            return BungeeChatConfig.fromConfig("role_reason");
        }

        @Override
        public SendToAddress getSendTo() {
            return SendToAddress.KICK;
        }
    },
    DISCORD_SYNCHRONIZATION{
        @Override
        public @NonNull TextComponent getKickReason() {
            return new TextComponent("");
        }

        @Override
        public SendToAddress getSendTo() {
            return SendToAddress.LOGIN_SERVER;
        }
    },
    REGISTRATION{
        @Override
        public @NonNull TextComponent getKickReason() {
            return new TextComponent("");
        }

        @Override
        public SendToAddress getSendTo() {
            return SendToAddress.LOGIN_SERVER;
        }
    },
    LOGIN{
        @Override
        public @NonNull TextComponent getKickReason() {
            return new TextComponent("");
        }

        @Override
        public SendToAddress getSendTo() {
            return SendToAddress.LOGIN_SERVER;
        }
    },
    VERIFIED{
        @Override
        public @NonNull TextComponent getKickReason() {
            return new TextComponent("");
        }

        @Override
        public SendToAddress getSendTo() {
            return SendToAddress.TARGET_SERVER;
        }
    };

    @Getter
    private SendToAddress sendTo;

    @NonNull
    @Getter
    private TextComponent kickReason;

    LoginState(){
    }

    private @Nullable TextComponent retrieveKickReason(){
        try {
            switch (this) {
                case PLAYER_UNKNOWN:
                    return BungeeChatConfig.fromConfig("not_in_whitelist");
                case NOT_IN_GUILD:
                    return BungeeChatConfig.fromConfig("quit_discord");
                case NO_ROLE:
                    return BungeeChatConfig.fromConfig("role_reason");
                case UNKNOWN_STATE:
                    return BungeeChatConfig.fromConfig("unknown_reason");
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Defines player's registration loginState from current data in AuthPlayer instance.
     * @param player AuthPlayer instance to provide
     * @return LoginState of provided player
     */
    //TODO: improve it
    public static LoginState defineState(@Nullable AuthPlayer player){
        if(player == null || !player.isInDatabase())
            return PLAYER_UNKNOWN;

        if(player.getDiscordID() == null || player.getDiscordID().equals("null") || player.getDiscordID().isEmpty())
            return DISCORD_SYNCHRONIZATION;

        if(player.getHashedPassword() == null || !player.getHashedPassword().isComplete())
            return REGISTRATION;

        if(player.getLastIP() == null || player.getLastIP().equals("null") || player.getLastIP().isEmpty())
            return LOGIN;

        return VERIFIED;
    }

    //FIXME: not working properly. Why?
    private SendToAddress defineDestinationTarget(){
        try {
            if(this == null) throw new NullPointerException();
            switch (this) {
                case VERIFIED:
                    return SendToAddress.TARGET_SERVER;

                case NO_ROLE:
                case UNKNOWN_STATE:
                case PLAYER_UNKNOWN:
                case NOT_IN_GUILD:
                    return SendToAddress.KICK;

                default:
                    return SendToAddress.LOGIN_SERVER;
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }
}
