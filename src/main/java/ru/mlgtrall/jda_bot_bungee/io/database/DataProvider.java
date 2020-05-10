package ru.mlgtrall.jda_bot_bungee.io.database;

public enum DataProvider {
    SQL("SQL"), YML("YML");
    String name;

    DataProvider(){this.name=this.name();}
    DataProvider(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
