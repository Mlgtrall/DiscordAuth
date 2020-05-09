package ru.mlgtrall.jda_bot_bungee.bungee.listener;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.ServersList;

public class ServerConnectEventListener implements Listener {
    Main plugin;
    public ServerConnectEventListener(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerConnectEvent(@NotNull ServerConnectEvent e){

        ProxiedPlayer player = e.getPlayer();
        ServerConnectEvent.Reason reason = e.getReason();
        if(reason.equals(ServerConnectEvent.Reason.KICK_REDIRECT)){
            TextComponent textComponent = new TextComponent(ChatColor.DARK_RED + "Вы были кикнуты");
            textComponent.addExtra("\n" + reason.toString());
            player.disconnect(textComponent);
            plugin.getVerifiedMembers().remove(player.getUniqueId());
        }


    }
}
