package ru.mlgtrall.jda_bot_bungee.bungee.listener;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.ServersList;
import ru.mlgtrall.jda_bot_bungee.bungee.connection.Connection;
import ru.mlgtrall.jda_bot_bungee.bungee.util.ChatManager;
import ru.mlgtrall.jda_bot_bungee.bungee.util.TitleManager;
import ru.mlgtrall.jda_bot_bungee.io.ConfigFiles;
import ru.mlgtrall.jda_bot_bungee.io.FileLoader;
import ru.mlgtrall.jda_bot_bungee.io.config.ConfigFile;
import ru.mlgtrall.jda_bot_bungee.io.database.YMLKeys;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ServerConnectedListener implements Listener {
    Main pl;
    public ServerConnectedListener(@NotNull final Main plugin){
        this.pl = plugin;

    }

    @EventHandler
    public void onServerConnected(@NotNull final ServerConnectedEvent event){

        //Check if connected server in plugin's servers list
//        for(Main.ServersList server : Main.ServersList.values()){
//            String name = server.getName();
//            if(!event.getServer().getInfo().getName().equals(name)) return;
//        }

        if (!ServersList.isAuthorizedServer(event.getServer().getInfo().getName())) return;


        final ProxiedPlayer player = event.getPlayer();
        final Map<String, ServerInfo> servers =  pl.getProxy().getServers();
        Set<UUID> verifiedMembers = pl.getVerifiedMembers();
        TaskScheduler scheduler = pl.getProxy().getScheduler();

        pl.getLogger().info("ServerConnected event = " + event.toString());

        FileLoader fileLoader = pl.getFileLoader();
        ConfigFile playerDBFile = fileLoader.get(ConfigFiles.PLAYER_DB_YML);
        Configuration playerDataBase = playerDBFile.getConfig();
        final UUID uuid = player.getUniqueId();
        String playerName = player.getName();


        scheduler.schedule(pl, () -> {
            try {
                if(!verifiedMembers.contains(uuid) && !servers.get(ServersList.LOGIN.getName()).getPlayers().contains(player)) {
                    pl.getLogger().info("Player is not in whitelist but on a Main server!");
                    Connection.tryConnect(player, ServersList.LOGIN.getName());
                    return;
                }

                if(verifiedMembers.contains(uuid) && servers.get(ServersList.LOGIN.getName()).getPlayers().contains(player)) {
                    pl.getLogger().info("Player is in whitelist, but in login server!");
                    Connection.tryConnect(player);

                    return;
                }
            }catch (NullPointerException ignored){ }

                if(servers.get(ServersList.LOGIN.getName()).getPlayers().contains(player)) {

                    scheduler.schedule(pl, () -> {
                        try {
                            if (pl.getProxy().getPlayer(uuid).isConnected() && !verifiedMembers.contains(uuid)) {
                                Connection.kick(player, ChatManager.fromConfig("timeout"));
                            }
                        } catch (NullPointerException ignored){}
                    }, 3, TimeUnit.MINUTES);

                    if(playerDataBase.contains(YMLKeys.PASSWORD.addBeforePath(playerName).getPath()) ||
                            playerDataBase.contains(YMLKeys.PASSWD_HASH.addBeforePath(playerName).getPath()) &&
                                    playerDataBase.contains(YMLKeys.SALT.addBeforePath(playerName).getPath())) {
                        scheduler.schedule(pl, () -> TitleManager.send(player, ChatManager.fromConfigRaw("title_login")), 2, TimeUnit.SECONDS);
                        player.sendMessage(ChatManager.fromConfig("join3"));
                        return;
                    }

                    if(playerDataBase.contains(YMLKeys.DISCORD_ID.addBeforePath(playerName).getPath())) {
                        scheduler.schedule(pl, () -> TitleManager.send(player, ChatManager.fromConfigRaw("title_reg")), 2, TimeUnit.SECONDS);
                        player.sendMessage(ChatManager.fromConfig("join_2"));
                    } else {
                        scheduler.schedule(pl, () -> TitleManager.send(player, ChatManager.fromConfigRaw("title_auth")), 2, TimeUnit.SECONDS);
                        player.sendMessage(ChatManager.fromConfig("join"));
                    }
                    return;
                }

                if (playerDataBase.contains(YMLKeys.LOGIN_IP.addBeforePath(playerName).getPath())){
                    scheduler.schedule(pl, () -> TitleManager.send(player, ChatManager.fromConfigRaw("title_welcome")), 2, TimeUnit.SECONDS);
                    //player.sendMessage(chatUtils.fromConfig("by_ip", true));
                }


            },1 , TimeUnit.SECONDS );




        /*if(verifiedMembers.contains(uuid)) {
            player.sendMessage(chatUtils.fromConfig("welcome"));
            return;
        }*/

    }
}
