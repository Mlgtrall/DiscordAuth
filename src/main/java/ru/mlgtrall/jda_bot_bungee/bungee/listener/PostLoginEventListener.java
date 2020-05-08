package ru.mlgtrall.jda_bot_bungee.bungee.listener;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.bungee.connection.Connection;
import ru.mlgtrall.jda_bot_bungee.bungee.util.ChatManager;
import ru.mlgtrall.jda_bot_bungee.io.ConfigFiles;
import ru.mlgtrall.jda_bot_bungee.io.FileLoader;
import ru.mlgtrall.jda_bot_bungee.io.config.ConfigFile;
import ru.mlgtrall.jda_bot_bungee.io.config.YMLKeys;
import ru.mlgtrall.jda_bot_bungee.jda.BotFactory;

import java.util.*;
import java.util.logging.Logger;

public class PostLoginEventListener implements Listener {
    private final Main plugin;
    BotFactory botAssembler;
    public PostLoginEventListener(@NotNull final Main plugin) {
        this.plugin = plugin;
        botAssembler = plugin.getBotAssembler();
    }

    @EventHandler
    public void onPostLoginEvent(@NotNull final PostLoginEvent event){

        final ProxiedPlayer player = event.getPlayer();
        Logger logger = plugin.getLogger();
        final UUID uuid = player.getUniqueId();
        FileLoader fileLoader = plugin.getFileLoader();
        ConfigFile playerDBFile = fileLoader.get(ConfigFiles.PLAYER_DB_YML);
        Configuration playerDB = playerDBFile.getConfig();
//        final Map<Main.ConfigKeys, String> config_keys =  plugin.getConfigKeys();
        final String playerName = player.getName();
        List<UUID> verifiedmembers = plugin.getVerifiedMembers();

        //logger.info("Post login event got triggered by " + player.getDisplayName() + " with UUID: " + player.getUniqueId() + " | [DEBUG]");
        //logger.info("Checking if " + player.getDisplayName() + " in player db | [DEBUG]");

        if(!playerDB.contains(playerName)){
            Connection.kick(player, ChatManager.fromConfig("not_in_whitelist"));
            return;
        }

        //Kick if tech works on


        //Cancelling deleting from playerDB
        HashMap<String , Integer> nameTaskIdList = plugin.getNameTaskIdList();
        if(nameTaskIdList.containsKey(playerName)) {
            int taskid = nameTaskIdList.get(playerName);
            plugin.getProxy().getScheduler().cancel(taskid);
            nameTaskIdList.remove(playerName);
        }

        //Discord Check
        if(playerDB.contains(YMLKeys.DISCORD_ID.addBeforePath(playerName).getPath())){

            JDA jda = plugin.getBotAssembler().getJDA();
            Guild guild = plugin.getBotAssembler().getGuild();
            String userid = playerDB.getString(YMLKeys.DISCORD_ID.addBeforePath(playerName).getPath());

            Member member = null;
            try {
                member = guild.getMemberById(userid);
            } catch (NullPointerException e){
                e.printStackTrace();
            }

            //If player quit discord
            if (member == null) {
                Connection.kick(player, ChatManager.fromConfig("quit_discord"));
                playerDB.set(playerName, null);
                playerDBFile.save();
                return;
            }

            //Adding nickname
//            playerDB.set(playerName+"."+config_keys.get(Main.ConfigKeys.DISCORD_NICKNAME), member.getNickname());
            playerDB.set(YMLKeys.DISCORD_NICKNAME.addBeforePath(playerName).getPath(), member.getNickname());
            //Adding Effective Name
            playerDB.set(YMLKeys.DISCORD_ENAME.addBeforePath(playerName).getPath(), member.getEffectiveName());

            List<Role> memberRoles = member.getRoles();
            List<Role> requiredRoles = new ArrayList<>();

            //Adding required roles from Id/name list as Role
            for(String stringRole : botAssembler.getRequiredRoleList()){
                try {
                    //logger.info("Adding role in list = " + stringRole);
                    requiredRoles.add(guild.getRoleById(stringRole));
                    //logger.info("Adding roles by name = " + guild.getRolesByName(stringRole, true));
                    requiredRoles.addAll(guild.getRolesByName(stringRole, true));
                }catch (NumberFormatException ignored){ ; }
            }

            //Contains required role?
            boolean memberHasRequiredRole = false;
            for(Role role : memberRoles){
                if (requiredRoles.contains(role)) {
                    memberHasRequiredRole = true;
                    break;
                }
            }

            //Checking for needed role
            if (!memberHasRequiredRole) {
                Connection.kick(player, ChatManager.fromConfig("role_reason"));
//                playerDB.set(playerName + config_keys.get(Main.ConfigKeys.DISCORD_ID), null);
                playerDB.set(YMLKeys.DISCORD_ID.addBeforePath(playerName).getPath(), null);
                playerDBFile.save();
                return;
            }
        }



        //IP
        String ip = player.getSocketAddress().toString().replaceAll("\\.","_").split(":")[0];
        logger.info("Player yamled ip = " + ip);

//        playerDB.set(playerName + "." + config_keys.get(Main.ConfigKeys.LAST_DISPLAY_NAME), player.getDisplayName());
        playerDB.set(YMLKeys.LAST_DISPLAY_NAME.addBeforePath(playerName).getPath(), player.getDisplayName());
        playerDBFile.save();



        //IP CHECK
//        if(playerDB.get(playerName + "." + config_keys.get(Main.ConfigKeys.LOGIN_IP) + "." + ip) == null){
//            logger.info("FALSE");
//            verifiedmembers.remove(player.getUniqueId());
//            return;
//        }

        if(playerDB.get(YMLKeys.LOGIN_IP.addBeforePath(playerName).addToPath(ip).getPath()) == null){
            logger.info("FALSE");
            verifiedmembers.remove(player.getUniqueId());
            return;
        }


        logger.info("TRUE");
        verifiedmembers.add(player.getUniqueId());
        //plugin.tryConnect(player);

    }
}
