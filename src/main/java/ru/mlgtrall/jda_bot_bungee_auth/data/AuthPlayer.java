package ru.mlgtrall.jda_bot_bungee_auth.data;

import lombok.Builder;
import lombok.Data;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.KeyHolder;
import ru.mlgtrall.jda_bot_bungee_auth.security.HashedPassword;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/*
    Bean class of a player data
 */

@Data
@Builder(toBuilder = true)
public final class AuthPlayer implements Serializable {

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
}
