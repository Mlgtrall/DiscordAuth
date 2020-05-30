package ru.mlgtrall.jda_bot_bungee.security;

import lombok.Data;

@Data
public class HashedPassword {

    private String hash;

    private String salt;

    public HashedPassword(String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }

    public HashedPassword(String salt){
        this.hash = null;
        this.salt = salt;
    }

}
