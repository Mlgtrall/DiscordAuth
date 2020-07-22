package ru.mlgtrall.jda_bot_bungee_auth.data.login;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.DataSource;

/**
 * This class represents an authorization session of a connecting player.
 * Session object helps to interact with player's data while session exists.
 */

@Builder(builderClassName = "Builder", toBuilder = true)
@Data
public final class LoginSession {

//    int sessionId;
    private AuthPlayer authPlayer;

    private boolean playerDefined;

    private LoginState state;

    public @NotNull PlayerDefiner playerDefiner(){
        return new PlayerDefiner(this);
    }


}
