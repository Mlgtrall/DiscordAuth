package ru.mlgtrall.jda_bot_bungee.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee.security.HashedPassword;

import java.util.UUID;

/*
    Bean class of a player data
 */

@Data
public final class AuthPlayer {

    private String name;
    private UUID uuid;
    private HashedPassword hashedPassword;
    private String discordID;
    private String regIP;
    private String lastIP;
    private String regDate;
    private String lastDate;
    private String country;
    private boolean discordVerification;

    public AuthPlayer(@NotNull String name){
        this.name = name;
    }


    public boolean isDiscordLinked(){
        return discordID != null && !discordID.isEmpty();
    }

    public @Nullable ProxiedPlayer getPlayer(){
        for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
            if(p.getName().equals(this.name)){
                return p;
            }
        }
        return null;
    }
}
