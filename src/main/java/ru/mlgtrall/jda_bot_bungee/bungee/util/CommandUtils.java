package ru.mlgtrall.jda_bot_bungee.bungee.util;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.mlgtrall.jda_bot_bungee.Main;

public class CommandUtils {
    public static final Main plugin = Main.getInstance();

    public static boolean isPlayer(CommandSender sender){
        if(!(sender instanceof ProxiedPlayer)){
            plugin.getLogger().info("only player can execute this command!");
            return true;
        }
        return false;
    }
}
