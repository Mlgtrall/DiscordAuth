package ru.mlgtrall.discordauth.security;

import lombok.*;

@EqualsAndHashCode
@Getter
@Setter
@ToString
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

    public boolean isComplete(){
        return hash != null && salt != null;
    }

}
