package ru.mlgtrall.discordauth.discord.command;

import com.tjplaysnow.discord.object.ProgramCommand;
import net.dv8tion.jda.core.entities.*;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.DiscordAuth;
import ru.mlgtrall.discordauth.data.AuthPlayer;
import ru.mlgtrall.discordauth.service.DiscordBotService;
import ru.mlgtrall.discordauth.io.database.DataSource;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import ru.mlgtrall.discordauth.io.log.ConsoleLoggerFactory;
import ru.mlgtrall.discordauth.util.DiscordConfig;

import javax.inject.Inject;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AuthMeCommand extends ProgramCommand {

    private final ConsoleLogger log = ConsoleLoggerFactory.get(this.getClass());

    @Inject
    private DiscordAuth pl;

    @Inject
    private DiscordBotService botService;

    @Inject
    private DataSource db;

    @Inject
    private TaskScheduler scheduler;

    public AuthMeCommand(){ }

    @Override
    public String getLabel() {
        return "authme";
    }

    //TODO: configurable
    @Override
    public String getDescription() {
        return "Команда для регистрации на сервере.";
    }

    @Override
    protected int getDeleteTime() {
        return 20;
    }

    @Override
    protected boolean run(@NotNull User user, @NotNull MessageChannel channel, @NotNull Guild guild, @NotNull String label, @NotNull List<String> args) {
        if(!botService.hasListenableChannel(channel)){
            log.info("Command was called not from listenable channel");
            return false;
        }

//        log.info("Discord user " + user.getName() + " executed " + label + " command");
//        log.info("Label = " + label);
//        log.info("Args = " + args);

        Member member = guild.getMember(user);
        if(member == null) return false;

        if(!botService.hasRequiredRole(member)){
            channel.sendMessage(DiscordConfig.fromConfig("no_right")).queue();
            return false;
        }

        //Wrong args size
        if(args.size() != 1){
            channel.sendMessage(DiscordConfig.fromConfig("dis_wrong_args")).queue();
            return false;
        }

        String playerName = args.get(0);
        //TODO: make better solution of this
        if(db.containsPlayer(playerName)){
            channel.sendMessage(DiscordConfig.fromConfig("already_auth")).queue();
            return false;
        }

        Date now = new Date();
        AuthPlayer player = AuthPlayer.builder().name(playerName).regDate(now.toString()).build();

        scheduler.runAsync(pl, () -> {
            db.savePlayer(player);
        });

        HashMap<String, Integer> nameTaskIdList = pl.getNameTaskIdMap();

        //Schedule deleting player from db if he will not join minecraft before delayed amount of time
        int taskId = scheduler.schedule(pl, () -> {
            log.info("Deleting player with minecraft nick: \"" + playerName + "\" from database because he reached timeout after discord registration.");
            db.deletePlayer(player.getName());
        }, 20, TimeUnit.MINUTES).getId();
        //TODO: move this to memory?
        nameTaskIdList.put(playerName, taskId);

        channel.sendMessage(DiscordConfig.fromConfig("private_message")).queue();
        String rndcode = new SecureRandom().nextInt(800000)+200000+"AA"; // Random code for authorizing players
        user.openPrivateChannel().complete().sendMessage(DiscordConfig.fromConfig("in_game_code","<rndcode>", rndcode)).queue();

        //Bond Auth code and Minecraft name in local memory map //TODO: move it to memory?
        pl.getNameCodeMap().put(playerName, rndcode);
        //Bond Minecraft Name with Discord ID in local memory map
        pl.getNameMineIdDiscordMap().put(playerName, user.getId());

        return false;
    }
}
