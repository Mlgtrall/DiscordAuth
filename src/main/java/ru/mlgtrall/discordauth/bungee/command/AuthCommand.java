package ru.mlgtrall.discordauth.bungee.command;

import net.dv8tion.jda.core.entities.Member;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import ru.mlgtrall.discordauth.DiscordAuth;
import ru.mlgtrall.discordauth.ServersList;
import ru.mlgtrall.discordauth.bungee.connection.Connection;
import ru.mlgtrall.discordauth.data.AuthPlayer;
import ru.mlgtrall.discordauth.data.AuthedPlayers;
import ru.mlgtrall.discordauth.io.database.DataSource;
import ru.mlgtrall.discordauth.util.BungeeChatConfig;
import ru.mlgtrall.discordauth.util.TitleManager;
import ru.mlgtrall.discordauth.service.DiscordBotService;
import ru.mlgtrall.discordauth.util.DiscordConfig;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static ru.mlgtrall.discordauth.util.BungeeCommandUtils.isPlayer;

public class AuthCommand extends Command {

    @Inject
    private DiscordAuth pl;

    @Inject
    private DiscordBotService botService;

    @Inject
    private DataSource db;

    @Inject
    private AuthedPlayers verified;

    @Inject
    private TaskScheduler scheduler;

    @Inject
    private ServersList serversList;


    public AuthCommand(){
        super("authme", "discordauth.authme");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!isPlayer(sender)){
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        ServerInfo serverInfo = player.getServer().getInfo();

        //Check for servers
        if(!serversList.isAuthorizedServer(serverInfo.getName())) return;
        if(!serversList.isLoginServer(serverInfo.getName())){
            //TODO: add configurable message
            player.sendMessage(new TextComponent("Вы уже авторизованы!"));
            return;
        }


        String name = player.getName();
        UUID uuid = player.getUniqueId();

        AuthPlayer authPlayer = db.getPlayer(name);
        if(authPlayer == null) return;

        //TODO: add more advanced state checking
        //Check for player data in cache
        if(authPlayer.getDiscordID() != null || verified.isAuthed(name)){
            player.sendMessage(BungeeChatConfig.fromConfig("ALREADY_AUTH", true));
            return;
        }

        if(args.length != 1){
            TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_auth"));
            player.sendMessage(BungeeChatConfig.fromConfig("WRONG_AUTH_ARGS", true));
            return;
        }

        //TODO: Generally move it to memory
        Map<String, String> nameCodeMap = pl.getNameCodeMap();
        //Check if code valid
        String actualcode = nameCodeMap.get(name);
        if(actualcode == null){
            return;
        }

        if(!actualcode.equals(args[0])){
            TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_auth"));
            player.sendMessage(BungeeChatConfig.fromConfig("wrong_code", true));
            return;
        }

       Map<String ,String > nameMineIdDiscordMap = pl.getNameMineIdDiscordMap();

        String discordid = nameMineIdDiscordMap.get(name);
        assert discordid != null;

        Member target = botService.getBot().getRelevantGuild().getMemberById(discordid);

        if(target == null){ //AuthPlayer left discord
            nameCodeMap.remove(name);
            nameMineIdDiscordMap.remove(name);

            //TODO: do better and add configurable reason to kick
            player.sendMessage(BungeeChatConfig.fromConfig("no_discord", true));
            scheduler.schedule(
                    pl,
                    () -> Connection.kick(player,"Вы вышли из гильдии дискорд! Для регистрации необходимо находиться в гильдии!"),
                    5,
                    TimeUnit.SECONDS
            );
            return;
        }

        authPlayer.setUuid(uuid);
        authPlayer.setDiscordID(discordid);
        //TODO: Add FutureCompletable?
        scheduler.runAsync(pl, () -> db.savePlayer(authPlayer));

        //Removing data from local HashMaps //TODO: move completely to memory from RAM?
        nameCodeMap.remove(name);
        nameMineIdDiscordMap.remove(name);

        //Message about successful authorization
        player.sendMessage(BungeeChatConfig.fromConfig("done_auth", true));
        target.getUser().openPrivateChannel().complete().sendMessage(DiscordConfig.fromConfig("done_auth")).queue();

        //Message about registration in minecraft
        TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_reg"));
        player.sendMessage(BungeeChatConfig.fromConfig("join_2", true));


    }


}
