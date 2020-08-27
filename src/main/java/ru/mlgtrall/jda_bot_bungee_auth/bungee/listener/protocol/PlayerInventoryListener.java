package ru.mlgtrall.jda_bot_bungee_auth.bungee.listener.protocol;

import de.exceptionflug.protocolize.api.event.PacketReceiveEvent;
import de.exceptionflug.protocolize.api.event.PacketSendEvent;
import de.exceptionflug.protocolize.api.handler.PacketAdapter;
import de.exceptionflug.protocolize.api.protocol.Stream;
import de.exceptionflug.protocolize.inventory.event.InventoryOpenEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.packet.PlayerListItem;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.data.AuthedPlayers;

import javax.inject.Inject;

public class PlayerInventoryListener implements Listener {

    private final AuthedPlayers verify;

    @Inject
    public PlayerInventoryListener(final Main plugin, final AuthedPlayers verify){
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
