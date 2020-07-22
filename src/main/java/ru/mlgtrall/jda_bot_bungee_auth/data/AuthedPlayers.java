package ru.mlgtrall.jda_bot_bungee_auth.data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a cache of authorized online players
 */
public class AuthedPlayers {

    /**
     * Key is minecraft player's name
     */
    private final Map<String, AuthPlayer> cache = new ConcurrentHashMap<>();

    public AuthedPlayers(){ }

    public void add(AuthPlayer player){
        cache.put(player.getName(), player);
    }


    public void remove(String name){
        cache.remove(name);
    }


    public boolean isAuthed(String name){
        return cache.containsKey(name);
    }

    public AuthPlayer getAuthPlayer(String name){
        return cache.get(name);
    }

    public int getAmount(){
        return cache.size();
    }

    public Map<String, AuthPlayer> getCache(){
        return this.cache;
    }

}
