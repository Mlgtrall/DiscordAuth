package ru.mlgtrall.jda_bot_bungee.io.database;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public final class AuthPlayer {

    String Name;
    String UUID;
    String hash;
    String salt;
    String discordID;
    String regIP;
    String lastIP;
    String regDate;
    String lastDate;
    String country;

    public AuthPlayer(){

    }
}
