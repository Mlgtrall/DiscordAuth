package ru.mlgtrall.discordauth.message;

import lombok.Getter;

public class MessageKeys {

    public enum Connection implements MessageKey{
        TRYING_TO_CONNECT,
        CAN_NOT_CONNECT,
        TRYING_TO_CONNECT_AGAIN,
        SERVER_OFFLINE;

        @Getter
        private final String key;

        Connection(){
            this.key = "connection." + this.name().toLowerCase();

        }

        Connection(String key){
            this.key = key;
        }
    }

    public enum Kick implements MessageKey{
        TRIES_LIMIT,
        FAILED_TO_CONNECT,
        QUIT_DISCORD,
        ROLE_REASON,
        NOT_IN_WHITELIST,
        TIMEOUT,
        UNKNOWN_REASON
        ;

        @Getter
        private final String key;

        Kick(){
            this.key =  "kick." + this.name().toLowerCase();
        }

        Kick(String key){
            this.key = key;
        }
    }

    public enum Title implements MessageKey{
        LOGIN_COMMAND,
        REG_COMMAND,
        AUTHME_COMMAND,
        WRONG_PASSWORD,
        WELCOME_MESSAGE
        ;

        @Getter
        private final String key;

        Title(){
            this.key = "title." + this.name().toLowerCase();
        }

        Title(String key){
            this.key = key;
        }
    }

    public enum OnAuthMeCommand implements MessageKey{
        REQUIRE_VERIFICATION,
        WRONG_ARGS,
        ALREADY_AUTHED,
        NOT_IN_LIST,
        WRONG_VERIFICATION_CODE,
        NOT_LINKED_WITH_DISCORD,
        DONE,
        DIRECT_PLAYER_TO_REG_COMMAND
        ;

        private final String key;

        OnAuthMeCommand(){
            this.key = "commands.authme." + this.name().toLowerCase();
        }

        OnAuthMeCommand(String key){
            this.key = key;
        }
    }

    public enum OnRegCommand implements MessageKey{
        NEED_AUTH,
        ALREADY_REGISTERED,
        WRONG_ARGS,
        PASSWORDS_NOT_EQUAL,
        PASSWORD_TOO_LONG,
        PASSWORD_TOO_SMALL,
        PASSWORD_INCORRECT_CHAR,
        DONE
        ;

        private final String key;

        OnRegCommand(){
            this.key = "commands.reg." + this.name().toLowerCase();
        }

        OnRegCommand(String key){
            this.key = key;
        }
    }

    public enum OnLoginCommand implements MessageKey{
        NEED_REG,
        ALREADY_LOGGED,
        REQUIRE_LOGIN,
        WRONG_ARGS,
        WRONG_PASSWORD,
        DONE,
        REMEMBERED_BY_IP
        ;

        private final String key;

        OnLoginCommand(){
            this.key = "commands.login." + this.name().toLowerCase();
        }

        OnLoginCommand(String key){
            this.key = key;
        }
    }

    public enum Other implements MessageKey{
        DO_NOT_HAVE_RIGHT_TO_TEXT_WHILE_NOT_LOGGED
        ;

        private final String key;

        Other(){
            this.key = "other." + this.name().toLowerCase();
        }

        Other(String key){
            this.key = key;
        }
    }

}
