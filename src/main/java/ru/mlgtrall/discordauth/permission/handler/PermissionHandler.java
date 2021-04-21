package ru.mlgtrall.discordauth.permission.handler;

import ru.mlgtrall.discordauth.data.AuthPlayer;
import ru.mlgtrall.discordauth.permission.PermissionNode;

import java.util.Collection;

public interface PermissionHandler {

    boolean addToGroup(AuthPlayer player, String group);

    boolean hasGroupSupport();

    boolean hasPermission(String playerName, PermissionNode permissionNode);

    boolean isInGroup(String playerName, String group);

    boolean removeFromGroup(String playerName, String group);

    boolean setGroup(String playerName, String group);

    Collection<String> getGroups(String playerName);

//    default String getPrimaryGroup(String playerName){
//        Collection<String> g = getGroups(playerName);
//
//    }


}
