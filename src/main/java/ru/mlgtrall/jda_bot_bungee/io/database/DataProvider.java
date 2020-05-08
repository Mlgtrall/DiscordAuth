package ru.mlgtrall.jda_bot_bungee.io.database;

public enum DataProvider {
    SQL("SQL"), YML("YML");
    String name;

    DataProvider(String name){
        this.name = name;
    }
    @Override
    public String toString(){
        return name;
    }
}
