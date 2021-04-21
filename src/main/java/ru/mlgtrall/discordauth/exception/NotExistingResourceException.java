package ru.mlgtrall.discordauth.exception;

public class NotExistingResourceException extends Exception{

    public NotExistingResourceException(String message){
        super(message);
    }

    public NotExistingResourceException(String message, Throwable cause){
        super(message, cause);
    }

}
