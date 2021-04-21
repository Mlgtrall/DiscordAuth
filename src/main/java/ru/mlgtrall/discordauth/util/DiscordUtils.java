package ru.mlgtrall.discordauth.util;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.DiscordAuth;

public final class DiscordUtils {

    private static final DiscordAuth pl = DiscordAuth.getInstance();

    public static boolean isUser(@NotNull GuildMessageReceivedEvent e){
        return !(e.getAuthor().isBot() || e.getAuthor().isFake() || e.isWebhookMessage());
    }

}
