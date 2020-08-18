package ru.mlgtrall.jda_bot_bungee_auth.bungee.connection;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.Servers;
import ru.mlgtrall.jda_bot_bungee_auth.util.BungeeChatConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Connection {

    private static final Main pl = Main.getInstance();

   private Connection(){}

    private static final Map<UUID, Integer> playerTriesMap = new ConcurrentHashMap<>();

    /**
     * DO NOT USE THIS WHILE PLAYER LOGGING IN!
     *
     * @param player
     * @param toServerByName
     */
    public static void tryConnect(@NotNull ProxiedPlayer player, String toServerByName){

        ServerInfo server = pl.getProxy().getServers().get(toServerByName);
        UUID uuid = player.getUniqueId();

        pl.getLogger().info("Trying to connect to "+ toServerByName +" server...");
        player.sendMessage(BungeeChatConfig.fromConfig("trying_to_connect",true));
        player.connect(pl.getProxy().getServers().get(toServerByName));
        pl.getLogger().info("AuthPlayer has sent to "+ toServerByName +" server!");

        pl.getProxy().getScheduler().schedule(pl, () -> {
            try {
                if (pl.getProxy().getPlayer(player.getUniqueId()).isConnected() && !server.getPlayers().contains(player)) {
                    playerTriesMap.put(uuid, 0);
                    player.sendMessage(BungeeChatConfig.fromConfig("can_not_connect"));
                    player.sendMessage(BungeeChatConfig.fromConfig("trying_to_connect_again"));
                    check(player, toServerByName);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        } , 5, TimeUnit.SECONDS);

    }

    public static void tryConnect(@NotNull ProxiedPlayer player) {
        tryConnect(player, Servers.WHITELIST.getName());
    }

    private static void check(@NotNull ProxiedPlayer player, String toServerByName) {

        final UUID uuid = player.getUniqueId();
        final ServerInfo server = pl.getProxy().getServers().get(toServerByName);
        AtomicInteger count = new AtomicInteger(playerTriesMap.get(uuid));

        player.sendMessage(BungeeChatConfig.fromConfig("can_not_connect", true));
        player.sendMessage(BungeeChatConfig.fromConfig("trying_to_connect_again"));

        pl.getProxy().getScheduler().schedule(pl, () -> {
            try {

                if (pl.getProxy().getPlayer(uuid).isConnected() && !server.getPlayers().contains(player)) {
                    player.sendMessage(BungeeChatConfig.fromConfig("trying_to_connect", true));
                    player.connect(pl.getProxy().getServers().get(toServerByName));
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            pl.getProxy().getScheduler().schedule(pl, () -> {
                try {
                    if (pl.getProxy().getPlayer(uuid).isConnected() && !server.getPlayers().contains(player)) {
                        count.incrementAndGet();
                        if (count.get() == 1) {
                            player.sendMessage(BungeeChatConfig.fromConfig("server_offline", true));
                        }

                        if (count.get() <= 6) {
                            playerTriesMap.put(uuid, count.intValue());
                            check(player, toServerByName);
                        } else {
                            kick(player, BungeeChatConfig.fromConfig("failed_to_connect"));
                            playerTriesMap.remove(uuid); // not necessary ?
                        }
                    }
                    playerTriesMap.remove(uuid);
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }, 10, TimeUnit.SECONDS);
            playerTriesMap.remove(uuid);
        },10,TimeUnit.SECONDS);
        count.set(0); // not necessary
        playerTriesMap.remove(uuid);
    }

    public static void kick(@NotNull ProxiedPlayer player, String reason){
        player.disconnect(new TextComponent(reason));
    }


    public static void kick(@NotNull ChatEvent event, String reason){
        event.getSender().disconnect(new TextComponent(reason));
    }

    public static void kick(@NotNull ProxiedPlayer player, TextComponent reason){
        player.disconnect(new TextComponent(reason));
    }


    public static void kick(@NotNull ChatEvent event, TextComponent reason){
        event.getSender().disconnect(new TextComponent(reason));
    }
}
