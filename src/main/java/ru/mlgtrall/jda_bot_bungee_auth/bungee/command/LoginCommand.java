package ru.mlgtrall.jda_bot_bungee_auth.bungee.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.bungee.connection.Connection;
import ru.mlgtrall.jda_bot_bungee_auth.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee_auth.data.AuthedPlayers;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.DataSource;
import ru.mlgtrall.jda_bot_bungee_auth.security.Hash;
import ru.mlgtrall.jda_bot_bungee_auth.util.BungeeChatConfig;
import ru.mlgtrall.jda_bot_bungee_auth.util.StringUtil;
import ru.mlgtrall.jda_bot_bungee_auth.util.TitleManager;

import javax.inject.Inject;
import java.util.Date;
import java.util.UUID;

import static ru.mlgtrall.jda_bot_bungee_auth.Servers.isAuthorizedServer;
import static ru.mlgtrall.jda_bot_bungee_auth.util.BungeeCommandUtil.isPlayer;
import static ru.mlgtrall.jda_bot_bungee_auth.util.StringUtil.socketAddressToIp;

public class LoginCommand extends Command {

    @Inject
    private Main pl;

    @Inject
    AuthedPlayers verified;

    @Inject
    private DataSource db;

    @Inject
    private TaskScheduler scheduler;

    public LoginCommand() {
        super("login", "", "l");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!isPlayer(sender)){
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        ServerInfo serverInfo = player.getServer().getInfo();
        if(!isAuthorizedServer(serverInfo.getName())){
            return;
        }

        String name = player.getName();

        AuthPlayer authPlayer = db.getPlayer(name);
        assert authPlayer != null;

        //check if already logged
        if(verified.isAuthed(name)){
            player.sendMessage(BungeeChatConfig.fromConfig("already_log", true));
            return;
        }

        //Checking player's state
        if(authPlayer.getHashedPassword().getHash() == null && authPlayer.getDiscordID() != null){
            player.sendMessage(BungeeChatConfig.fromConfig("need_reg", true));
            player.sendMessage(BungeeChatConfig.fromConfig("join2"));
            return;
        }else if(authPlayer.getDiscordID() == null){
            player.sendMessage(BungeeChatConfig.fromConfig("need_auth", true));
            player.sendMessage(BungeeChatConfig.fromConfig("join"));
            return;
        }


        if(args.length != 1){
            TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_login"));
            player.sendMessage(BungeeChatConfig.fromConfig("wrong_login_args", true));
            return;
        }

        //Check if password from message equal in database
        String password = args[0];
        String salt = authPlayer.getHashedPassword().getSalt();
        String hashedPassword = authPlayer.getHashedPassword().getHash();

        if(!Hash.generateHash(password, salt).equals(hashedPassword)){
            TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_login"));
            player.sendMessage(BungeeChatConfig.fromConfig("wrong_password", true));
            return;
        }

        player.sendMessage(BungeeChatConfig.fromConfig("login_ok", true));
        verified.add(authPlayer);

        //Teleport on needed server. In this case - main server
        Connection.tryConnect(player);

        //Current server date
        Date now = new Date();
        String ip = socketAddressToIp(player.getSocketAddress());

        authPlayer.setLastIP(ip);
        authPlayer.setLastDate(now.toString());
        scheduler.runAsync(pl, () -> db.savePlayer(authPlayer));

    }
}
