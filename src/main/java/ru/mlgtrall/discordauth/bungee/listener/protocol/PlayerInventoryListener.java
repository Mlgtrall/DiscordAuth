package ru.mlgtrall.discordauth.bungee.listener.protocol;

import de.exceptionflug.protocolize.inventory.event.InventoryOpenEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.DiscordAuth;
import ru.mlgtrall.discordauth.data.AuthedPlayers;

import javax.inject.Inject;

public class PlayerInventoryListener implements Listener {

    private final AuthedPlayers verify;

    @Inject
    public PlayerInventoryListener(final DiscordAuth plugin, final AuthedPlayers verify){
        ProxyServer.getInstance().getPluginManager().registerListener(plugin, this);
        this.verify = verify;
    }

    @EventHandler
    public void onOpeningInventory(final @NotNull InventoryOpenEvent e){
        ProxiedPlayer proxiedPlayer = e.getPlayer();
        if(proxiedPlayer == null){
            return;
        }

        String playerName = proxiedPlayer.getName();
        if(!verify.isAuthed(playerName)){
            e.setCancelled(true);
            return;
        }

    }



}
