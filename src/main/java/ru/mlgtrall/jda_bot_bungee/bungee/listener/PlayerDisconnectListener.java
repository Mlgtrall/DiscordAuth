package ru.mlgtrall.jda_bot_bungee.bungee.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;

public class PlayerDisconnectListener implements Listener {

    Main plugin;
    public PlayerDisconnectListener(final Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDisconnectEvent(@NotNull final PlayerDisconnectEvent event){
        final ProxiedPlayer player = event.getPlayer();
        plugin.getLogger().info("Disconnect event = " + event);

        plugin.getVerifiedMembers().remove(player.getUniqueId());

    }
}
