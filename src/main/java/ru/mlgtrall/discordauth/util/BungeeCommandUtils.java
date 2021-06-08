package ru.mlgtrall.discordauth.util;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.mlgtrall.discordauth.DiscordAuth;
import ru.mlgtrall.discordauth.bungee.command.ACFCommandManager;

public final class BungeeCommandUtils {
    public static final DiscordAuth plugin = DiscordAuth.getInstance();

    private BungeeCommandUtils(){}

    public static boolean isPlayer(CommandSender sender){
        if(!(sender instanceof ProxiedPlayer)){
            sender.sendMessage(new TextComponent("Only player can execute this command!"));
            return false;
        }
        return true;
    }

}
