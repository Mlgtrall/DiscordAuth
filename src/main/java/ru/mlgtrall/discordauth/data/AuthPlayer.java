package ru.mlgtrall.discordauth.data;

import lombok.Builder;
import lombok.Data;
import ru.mlgtrall.discordauth.security.HashedPassword;

import java.io.Serializable;
import java.util.UUID;

/**
 * A bean class that represents a main player data skeleton for a whole plugin.
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
