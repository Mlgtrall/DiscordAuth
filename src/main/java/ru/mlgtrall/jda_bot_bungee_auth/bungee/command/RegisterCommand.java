package ru.mlgtrall.jda_bot_bungee_auth.bungee.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.DataSource;
import ru.mlgtrall.jda_bot_bungee_auth.security.HashedPassword;
import ru.mlgtrall.jda_bot_bungee_auth.util.*;
import ru.mlgtrall.jda_bot_bungee_auth.security.Hash;

import javax.inject.Inject;
import java.util.Date;
import java.util.UUID;

import static ru.mlgtrall.jda_bot_bungee_auth.util.BungeeCommandUtil.isPlayer;
import static ru.mlgtrall.jda_bot_bungee_auth.util.StringUtil.socketAddressToIp;

public class RegisterCommand extends Command {

    @Inject
    private Main pl;

    @Inject
    private DataSource db;

    @Inject
    private TaskScheduler scheduler;

    public RegisterCommand(){
        super("reg","","r");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!isPlayer(sender)){
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        String playerName = player.getName();

        AuthPlayer authPlayer = db.getPlayer(playerName);
        assert authPlayer != null;

        //TODO: complete more advanced state checking + move to another class
        if(authPlayer.getDiscordID() != null && authPlayer.getHashedPassword().isComplete()){
            player.sendMessage();BungeeChatConfig.fromConfig("already_reg", true);
            return;
        }else if(authPlayer.getDiscordID() == null) {
            player.sendMessage(BungeeChatConfig.fromConfig("need_auth", true));
            player.sendMessage(BungeeChatConfig.fromConfig("join"));
            return;
        }

        if(args.length != 2){
            player.sendMessage(BungeeChatConfig.fromConfig("wrong_reg", true));
            TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_reg"));
            return;
        }

        //Check if passwords equal
        if(!args[0].equals(args[1])){
            player.sendMessage(BungeeChatConfig.fromConfig("password_not_equal",true));
            TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_reg"));
            return;
        }

        String password = PasswordUtil.checkIfValid(args[0], player);
        if(password == null) return;

        String salt = Hash.createSaltStr();
        String hash = Hash.generateHash(password, salt);

        HashedPassword completedPassword = new HashedPassword(hash, salt);
        authPlayer.setHashedPassword(completedPassword);

        //Message of reg is ok
        player.sendMessage(BungeeChatConfig.fromConfig("reg_ok"));

        Date now = new Date();
        String ip = socketAddressToIp(player.getSocketAddress());

        authPlayer.setRegIP(ip);
        //TODO: add completable future?
        scheduler.runAsync(pl, () -> db.savePlayer(authPlayer));

        //Message of login
        TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_login"));
        player.sendMessage(BungeeChatConfig.fromConfig("join3",true));
    }
}
