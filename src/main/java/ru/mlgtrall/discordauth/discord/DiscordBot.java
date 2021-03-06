package ru.mlgtrall.discordauth.discord;

import com.tjplaysnow.discord.object.Bot;
import lombok.Builder;
import lombok.Data;
import net.dv8tion.jda.core.entities.Guild;

/**
 * Discord bot entity class wrapper around TJ's Discord API with relevant guild instance.
 */
@Data
@Builder(builderClassName = "Builder", toBuilder = true)
public class DiscordBot {

    private Bot tjBot;

    private Guild relevantGuild;


}
