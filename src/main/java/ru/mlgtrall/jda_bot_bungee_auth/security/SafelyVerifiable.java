package ru.mlgtrall.jda_bot_bungee_auth.security;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public abstract class SafelyVerifiable {

    static <T> T verify(T data, ProxiedPlayer player) {
        return null;
    }

    static <T> T verify(T data){
        return null;
    }

    static <T> boolean confirm(T data, ProxiedPlayer player){
        return false;
    }

    static <T> boolean confirm(T data){
        return false;
    }

    <T> T validate(T data, ProxiedPlayer player){
        return null;
    }

    <T> T validate(T data){
        return null;
    }

    <T> boolean checkIfValid(T data, ProxiedPlayer player){
        return false;
    }

    <T> boolean checkIfValid(T data){
        return false;
    }

}
