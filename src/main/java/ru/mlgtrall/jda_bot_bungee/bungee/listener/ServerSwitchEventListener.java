package ru.mlgtrall.jda_bot_bungee.bungee.listener;

import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;

public class ServerSwitchEventListener implements Listener {

    Main plugin;
    public ServerSwitchEventListener(final Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerSwitchEvent(@NotNull final ServerSwitchEvent event){
        plugin.getLogger().info("ServerSwitch Event = " + event.toString());
    }
}
