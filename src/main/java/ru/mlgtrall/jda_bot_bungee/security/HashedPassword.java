package ru.mlgtrall.jda_bot_bungee.security;

import lombok.Data;
import lombok.Getter;

@Data
public class HashedPassword {

    @Getter
    private final String hash;

    @Getter
    private final String salt;

    public HashedPassword(String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }

//    public HashedPassword(String hash){
//        this(hash, null);
//    }

}
