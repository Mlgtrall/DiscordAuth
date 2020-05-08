package ru.mlgtrall.jda_bot_bungee.bungee.connection;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.bungee.util.ChatManager;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Connection {

    private static Main pl = Main.getInstance();

    public Connection(){
        pl = Main.getInstance();
        pl.getLogger().info("Registering connection instance...");
        playerTriesMap = new HashMap<>();
        pl.getLogger().info("Registering connection instance done!");
    }

    private static HashMap<UUID, Integer> playerTriesMap;

    public static void tryConnect(@NotNull ProxiedPlayer player, String toServerByName){

        ServerInfo server = pl.getProxy().getServers().get(toServerByName);
        UUID uuid = player.getUniqueId();

        pl.getLogger().info("Trying to connect to "+ toServerByName +" server...");
        player.sendMessage(ChatManager.fromConfig("trying_to_connect",true));
        player.connect(pl.getProxy().getServers().get(toServerByName));
        pl.getLogger().info("Player has sent to "+ toServerByName +" server!");

        pl.getProxy().getScheduler().schedule(pl, () -> {
            try {
                if (pl.getProxy().getPlayer(player.getUniqueId()).isConnected() && !server.getPlayers().contains(player)) {
                    playerTriesMap.put(uuid, 0);
                    player.sendMessage(ChatManager.fromConfig("can_not_connect"));
                    player.sendMessage(ChatManager.fromConfig("trying_to_connect_again"));
                    check(player, toServerByName);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        } , 5, TimeUnit.SECONDS);

    }

    public static void tryConnect(@NotNull ProxiedPlayer player) {

//        String toServerByName = pl.getServersListEnum().get(Main.ServersList.MAIN);
        String toServerByName = Main.ServersList.MAIN.toString();
        ServerInfo server = pl.getProxy().getServers().get(toServerByName);
        UUID uuid = player.getUniqueId();


        pl.getLogger().info("Trying to connect to Main server...");
        player.sendMessage(ChatManager.fromConfig("trying_to_connect",true));
        player.connect(pl.getProxy().getServers().get(toServerByName));
        pl.getLogger().info("Player has sent to Main server!");

        pl.getProxy().getScheduler().schedule(pl, () -> {
            try {
                if (pl.getProxy().getPlayer(uuid).isConnected() && !server.getPlayers().contains(player)) {
                    playerTriesMap.put(uuid, 0);
                    check(player, toServerByName);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        } , 5, TimeUnit.SECONDS);

    }

    private static void check(@NotNull ProxiedPlayer player, String toServerByName) {

        final UUID uuid = player.getUniqueId();
        final ServerInfo server = pl.getProxy().getServers().get(toServerByName);
        AtomicInteger count = new AtomicInteger(playerTriesMap.get(uuid));

        player.sendMessage(ChatManager.fromConfig("can_not_connect", true));
        player.sendMessage(ChatManager.fromConfig("trying_to_connect_again"));

        pl.getProxy().getScheduler().schedule(pl, () -> {
            try {

                if (pl.getProxy().getPlayer(uuid).isConnected() && !server.getPlayers().contains(player)) {
                    player.sendMessage(ChatManager.fromConfig("trying_to_connect", true));
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
                            player.sendMessage(ChatManager.fromConfig("server_offline", true));
                        }

                        if (count.get() <= 6) {
                            playerTriesMap.put(uuid, count.intValue());
                            check(player, toServerByName);
                        } else {
                            kick(player, ChatManager.fromConfig("failed_to_connect"));
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
