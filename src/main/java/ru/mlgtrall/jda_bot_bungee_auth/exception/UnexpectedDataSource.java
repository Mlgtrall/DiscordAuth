package ru.mlgtrall.jda_bot_bungee_auth.exception;

public class UnexpectedDataSource extends Exception {
    public UnexpectedDataSource(){
        super("Unexpected data source. You specify correct data source in plugin's settings.");
    }

    public UnexpectedDataSource(String message){
        super(message);
    }
}
