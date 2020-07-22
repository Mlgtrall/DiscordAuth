package ru.mlgtrall.jda_bot_bungee_auth.util;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.discord.DiscordBotService;

import java.util.List;

public final class DiscordUtils {

    private static final Main pl = Main.getInstance();

    public static boolean isUser(@NotNull GuildMessageReceivedEvent e){
        return !(e.getAuthor().isBot() || e.getAuthor().isFake() || e.isWebhookMessage());
    }

}
