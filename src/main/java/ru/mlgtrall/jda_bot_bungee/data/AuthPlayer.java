package ru.mlgtrall.jda_bot_bungee.data;

import lombok.Builder;
import lombok.Data;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee.io.database.DatabaseProvider;
import ru.mlgtrall.jda_bot_bungee.security.HashedPassword;

import java.util.UUID;

/*
    Bean class of a player data
 */

@Data
@Builder(toBuilder = true)
public final class AuthPlayer{

    private static final DatabaseProvider db = DatabaseProvider.currentDatabase();

    private String name;
    private UUID uuid;
    private HashedPassword hashedPassword;
    private String discordID;
    private String regIP;
    private String lastIP;
    private String regDate;
    private String lastDate;
    private String country;
    
    @Builder.Default
    private boolean inDatabase = false;

    private PlayerStates.AuthSessionState sessionState;

    public @Nullable ProxiedPlayer getOnlinePlayer(){
        for(ProxiedPlayer p : ProxyServer.getInstance().getPlayers()){
            if(p.getName().equals(this.name)){
                return p;
            }
        }
        return null;
    }
}
