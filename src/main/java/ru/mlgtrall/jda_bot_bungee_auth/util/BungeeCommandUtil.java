package ru.mlgtrall.jda_bot_bungee_auth.util;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.mlgtrall.jda_bot_bungee_auth.Main;

public final class BungeeCommandUtil {
    public static final Main plugin = Main.getInstance();

    private BungeeCommandUtil(){}

    public static boolean isPlayer(CommandSender sender){
        if(!(sender instanceof ProxiedPlayer)){
            sender.sendMessage(new TextComponent("Only player can execute this command!"));
            return false;
        }
        return true;
    }
}
