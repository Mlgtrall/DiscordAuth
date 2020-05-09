package ru.mlgtrall.jda_bot_bungee.bungee.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.ServersList;
import ru.mlgtrall.jda_bot_bungee.bungee.connection.Connection;
import ru.mlgtrall.jda_bot_bungee.bungee.util.ChatManager;
import ru.mlgtrall.jda_bot_bungee.bungee.util.CommandUtils;
import ru.mlgtrall.jda_bot_bungee.bungee.util.TitleManager;
import ru.mlgtrall.jda_bot_bungee.io.ConfigFiles;
import ru.mlgtrall.jda_bot_bungee.io.FileLoader;
import ru.mlgtrall.jda_bot_bungee.io.config.ConfigFile;
import ru.mlgtrall.jda_bot_bungee.io.config.YMLKeys;
import ru.mlgtrall.jda_bot_bungee.security.Hash;
import ru.mlgtrall.jda_bot_bungee.security.Password;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class LoginCommand extends Command {

    Main plugin;
    ChatManager chatManager;
    Connection connection;
    public LoginCommand(@NotNull Main plugin) {
        super("login", "", "l");
        this.plugin = plugin;

        //chatManager = plugin.getChatManager();
        //connection = plugin.getConnection();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(CommandUtils.isPlayer(sender))return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        ServerInfo serverInfo = player.getServer().getInfo();
        if(ServersList.isAuthorizedServer(serverInfo.getName())){

        }

        FileLoader fileLoader = plugin.getFileLoader();
        Set<UUID> verifiedMembers = plugin.getVerifiedMembers();
        ConfigFile playerDBFile = fileLoader.get(ConfigFiles.PLAYER_DB_YML);
        Configuration playerDB = playerDBFile.getConfig();
        UUID uuid = player.getUniqueId();
        String playerName = player.getName();

        //check if already logged
        if(verifiedMembers.contains(uuid)){
            player.sendMessage(ChatManager.fromConfig("already_log", true));
            return;
        }

        //Checking database
        if(!playerDB.contains(YMLKeys.PASSWORD.addBeforePath(playerName).getPath()) ||
                !playerDB.contains(YMLKeys.PASSWD_HASH.addBeforePath(playerName).getPath()) &&
                        playerDB.contains(YMLKeys.DISCORD_ID.addBeforePath(playerName).getPath())){
            player.sendMessage(ChatManager.fromConfig("need_reg", true));
            player.sendMessage(ChatManager.fromConfig("join2"));
            return;
        }else if(!playerDB.contains(YMLKeys.DISCORD_ID.addBeforePath(playerName).getPath())){
            player.sendMessage(ChatManager.fromConfig("need_auth", true));
            player.sendMessage(ChatManager.fromConfig("join"));
            return;
        }

        if(args.length != 1){
            TitleManager.send(player, ChatManager.fromConfigRaw("title_login"));
            player.sendMessage(ChatManager.fromConfig("wrong_login_args", true));
            return;
        }



        //Check if password from message equal in database
        String password = args[0];
        String salt;
        String hashedPassword;
        if(!playerDB.contains(YMLKeys.PASSWD_HASH.addBeforePath(playerName).getPath()) ||
                !playerDB.contains(YMLKeys.SALT.addBeforePath(playerName).getPath())) {

            if (!playerDB.getString(YMLKeys.PASSWORD.addBeforePath(playerName).getPath()).equals(password)) {
                TitleManager.send(player, ChatManager.fromConfigRaw("title_login"));
                player.sendMessage(ChatManager.fromConfig("wrong_password", true));
                return;
            }
        }else{
            salt = playerDB.getString(YMLKeys.SALT.addBeforePath(playerName).getPath());
            hashedPassword = playerDB.getString(YMLKeys.PASSWD_HASH.addBeforePath(playerName).getPath());
            if(!Hash.generateHash(password, salt).equals(hashedPassword)){
                TitleManager.send(player, ChatManager.fromConfigRaw("title_login"));
                player.sendMessage(ChatManager.fromConfig("wrong_password", true));
                return;
            }
        }

        player.sendMessage(ChatManager.fromConfig("login_ok", true));
        plugin.getVerifiedMembers().add(uuid);

        //Teleport on needed server. In this case - main server
        Connection.tryConnect(player);

        //Current server date
        Date now = new Date(System.currentTimeMillis());

        //Ip of player in format that can be contained in .yml
        String ip = player
                .getSocketAddress()
                .toString()
                .replaceAll("\\.","_")
                .split(":")[0];

        playerDB.set(YMLKeys.LOGIN_IP.addBeforePath(playerName).getPath(), ip);
        playerDB.set(YMLKeys.LAST_DISPLAY_NAME.addBeforePath(playerName).getPath(), player.getDisplayName());

        // playerDB.set(playerName+ ".COUNTRY", player.getLocale().getCountry()); //change for geoIp
        playerDBFile.save();

    }
}
