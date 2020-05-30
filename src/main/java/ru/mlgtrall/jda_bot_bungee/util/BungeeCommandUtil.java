package ru.mlgtrall.jda_bot_bungee.util;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.mlgtrall.jda_bot_bungee.Main;

public final class BungeeCommandUtil {
    public static final Main plugin = Main.getInstance();

    private BungeeCommandUtil(){}

    public static boolean isPlayer(CommandSender sender){
        if(!(sender instanceof ProxiedPlayer)){
            plugin.getLogger().info("only player can execute this command!");
            return true;
        }
        return false;
    }
}
