package ru.mlgtrall.jda_bot_bungee.jda.listener;


import com.tjplaysnow.discord.object.Bot;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.io.ConfigFiles;
import ru.mlgtrall.jda_bot_bungee.io.FileLoader;
import ru.mlgtrall.jda_bot_bungee.io.file.config.YMLConfigFile;
import ru.mlgtrall.jda_bot_bungee.io.database.YMLKeys;
import ru.mlgtrall.jda_bot_bungee.jda.BotFactory;
import ru.mlgtrall.jda_bot_bungee.jda.util.JDAConfigUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class GuildMessage extends ListenerAdapter {

    BotFactory botAssembler;
    Main plugin;
    Bot bot;
    List<String> requiredRoleList;
    public GuildMessage(@NotNull final Main plugin, @NotNull final BotFactory botAssembler){
        this.plugin = plugin;
        this.botAssembler = botAssembler;
        this.bot = botAssembler.getBot();
        requiredRoleList = botAssembler.getRequiredRoleList();

    }

    @Override
    public void onGuildMessageReceived(@NotNull final GuildMessageReceivedEvent e) {
        if(e.getAuthor().isBot() || e.getAuthor().isFake() || e.isWebhookMessage()) return;

        final TextChannel channel = e.getChannel();

        if(!botAssembler.getBotChannels().contains(channel.getId()))return; // listening only needed channels

        final String arg = e.getMessage().getContentRaw();
        final String[] args = arg.split(" "); // r!command arg arg arg ...
        final String prefix = bot.getPrefix();
        final String auth_command = JDAConfigUtils.fromConfig("auth_command");

        //Проверка на роль
        final List<Role> memberRoles = Objects.requireNonNull(e.getMember()).getRoles();
        boolean memberHasRequiredRole = false;
        for(Role role: memberRoles){
            if(requiredRoleList.contains(role.getId()) || requiredRoleList.contains(role.getName())){
                memberHasRequiredRole = true;
                break;
            }
        }

        if(args[0].contains(prefix) && args[0].contains(Objects.requireNonNull(auth_command))
                && memberHasRequiredRole){  //Check for required roles and if command valid

            if(args.length != 2) {
                channel.sendMessage(JDAConfigUtils.fromConfig("dis_wrong_args")).queue(); //.queue
                return;
            }



            final User user = e.getAuthor();
            final FileLoader fileLoader = plugin.getFileLoader();
            final String playerName = args[1];
            YMLConfigFile playerDBFile = fileLoader.get(ConfigFiles.PLAYER_DB_YML);
            Configuration playerDB = playerDBFile.getConfig();

            if(playerDB.contains(playerName)){
                channel.sendMessage(JDAConfigUtils.fromConfig("already_auth")).queue();
                return;
            }

            //Adding registration Date in database (server time)
            Date now = new Date(System.currentTimeMillis());
            playerDB.set(YMLKeys.REG_DATE.addBeforePath(playerName).getPath(), now.toString() );
            playerDBFile.save();

            //Deleting from database if didn't joined in 10 minutes
            TaskScheduler scheduler = plugin.getProxy().getScheduler();
            HashMap<String, Integer> nameTaskIdList = plugin.getNameTaskIdList();
            Runnable task = () -> {
                playerDB.set(playerName,null);
                playerDBFile.save();
            };

            int taskId = scheduler.schedule(plugin, task, 10, TimeUnit.MINUTES).getId();
            nameTaskIdList.put(task.toString(), taskId);



            channel.sendMessage(JDAConfigUtils.fromConfig("private_message")).queue();
            String rndcode = new Random().nextInt(800000)+200000+"AA"; // Генерация рандомного кода для уникального понимания что это тот самый человек
            user.openPrivateChannel().complete().sendMessage(JDAConfigUtils.fromConfig("in_game_code","<rndcode>", rndcode)).queue();


            //Bond Auth code and Minecraft name
            plugin.getNameCodeMap().put(playerName, rndcode);
            //Bond Minecraft Name with Discord ID
            plugin.getNameMineIdDiscordMap().put(playerName, user.getId());//to file ??

            //Now is in file
            //playerDB.set(playerName + "." + config_keys.get(Main.ConfigKeys.CODE), rndcode);
            //playerDB.set(playerName + "." + config_keys.get(Main.ConfigKeys.DISCORD_ID) , user.getId());


        } else if(args[0].contains(prefix) && args[0].contains(Objects.requireNonNull(auth_command))
                && !memberHasRequiredRole){

            channel.sendMessage(JDAConfigUtils.fromConfig("no_right")).queue();
        }
    }

}

