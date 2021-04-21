package ru.mlgtrall.discordauth.bungee.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.DiscordAuth;
import ru.mlgtrall.discordauth.bungee.connection.Connection;
import ru.mlgtrall.discordauth.data.AuthPlayer;
import ru.mlgtrall.discordauth.data.AuthedPlayers;
import ru.mlgtrall.discordauth.discord.DiscordBot;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import ru.mlgtrall.discordauth.io.log.ConsoleLoggerFactory;
import ru.mlgtrall.discordauth.service.DiscordBotService;
import ru.mlgtrall.discordauth.io.database.DataSource;
import ru.mlgtrall.discordauth.security.Hash;
import ru.mlgtrall.discordauth.security.HashedPassword;
import ru.mlgtrall.discordauth.security.Password;
import ru.mlgtrall.discordauth.util.BungeeChatConfig;
import ru.mlgtrall.discordauth.util.DiscordConfig;
import ru.mlgtrall.discordauth.util.TitleManager;

import javax.inject.Inject;
import java.security.SecureRandom;
import java.util.*;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static ru.mlgtrall.discordauth.Servers.isAuthorizedServer;
import static ru.mlgtrall.discordauth.Servers.isLoginServer;

public class BungeeCommands {

    static final ConsoleLogger log = ConsoleLoggerFactory.get(BungeeCommands.class);

    @Inject
    private DiscordBotService botService;

    @Inject
    private DataSource db;

    @Inject
    private TaskScheduler scheduler;

    @Inject
    private DiscordAuth pl;

    public static class RegistrationCommands extends BaseCommand{

        @Inject
        private DiscordAuth pl;

        @Inject
        private TaskScheduler scheduler;

        @Inject
        private AuthedPlayers verified;

        @Inject
        private DiscordBotService botService;

        @Inject
        private DataSource db;

        @CommandAlias("authme")
        @CommandPermission("discordauth.authme")
        public void onAuthMe(@NotNull ProxiedPlayer player, AuthPlayer authPlayer, String[] args){
            ServerInfo serverInfo = player.getServer().getInfo();

            //Check for servers
            if(!isAuthorizedServer(serverInfo.getName())) return;
            if(!isLoginServer(serverInfo.getName())){
                //TODO: add configurable message
                player.sendMessage(new TextComponent("Вы уже авторизованы!"));
                return;
            }


            String name = player.getName();
            UUID uuid = player.getUniqueId();

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

        @HelpCommand
        public void doHelp(CommandSender sender, @NotNull CommandHelp help) {
            help.showHelp();
        }
    }

    @CommandAlias("cp|changepassword|changepass")
    public static class ChangePasswordCommand extends BaseCommand{

        @Inject
        private DiscordBotService botService;

        @Inject
        private DataSource db;

        @Inject
        private TaskScheduler scheduler;

        @Inject
        private DiscordAuth pl;

        @Default
        @Syntax("<password> <password>")
        @Description("A command for changing password.")
        public void onChangePassword(@NotNull ProxiedPlayer player, String[] args){
            String playerName = player.getName();

            if(!confirmedIdentityPlayerNames.contains(playerName)){

                player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',"Вы не подтвердили свою личность! Введите сначала комманду " + ChatColor.RED + "/cp request discord")));
                return;
            }

            if(args.length != 2){
                player.sendMessage(new TextComponent("Неправильно использована команда! Повторите ввод."));
                throw new InvalidCommandArgument();
            }

            if(!Password.checkIfValid(args[0], player)){
                throw new InvalidCommandArgument();
            }

            if(!args[0].equals(args[1])){
                player.sendMessage(new TextComponent("Пароли не совпадают. Повторите ввод."));
                throw new InvalidCommandArgument();
            }

            String salt = Hash.createSaltStr();
            String hash = Hash.generateHash(args[0], salt);

            Optional<AuthPlayer> authPlayer = Optional.ofNullable(db.getPlayer(playerName));
            HashedPassword oldHashedPassword = authPlayer.map(AuthPlayer::getHashedPassword).orElse(null);

            HashedPassword hashedPassword = new HashedPassword(hash, salt);
            authPlayer.ifPresent(it -> it.setHashedPassword(hashedPassword));

            authPlayer.ifPresent(it -> db.savePlayer(it));

            log.fine("Password was changed for player " + playerName + " from " + oldHashedPassword + " to " + hashedPassword);

            scheduler.schedule(pl, () -> player.sendMessage(new TextComponent("Пароль успешно изменен.")), 1, TimeUnit.SECONDS);
            scheduler.schedule(pl, () -> confirmedIdentityPlayerNames.remove(playerName), 20, TimeUnit.MINUTES);
        }

        //Map to store [playerName,hisRequestCode]
        private final Map<String, String> requestCodes = new ConcurrentHashMap<>();

        @Subcommand("request")
        @Description("A command to request a password change mission.")
        @CommandCompletion("discord")
        @Syntax("[discord]")
        public void onChangePasswordRequest(@NotNull ProxiedPlayer player, @Default("discord") String arg){
            String playerName = player.getName();

            if(confirmedIdentityPlayerNames.contains(playerName)){
                player.sendMessage(new TextComponent("Вы уже подтвердили свою личность. Введите команду /cp <password> <password>, если вам требуется сменить пароль."));
            }
            if(requestCodes.containsKey(playerName)){
                player.sendMessage(new TextComponent("Вы уже запросили смену пароля. Подтвердите свою личность, как было указано при запросе."));
                return;
            }

            Optional<AuthPlayer> authPlayer = Optional.ofNullable(db.getPlayer(playerName));
            if(!authPlayer.isPresent()){
                //TODO: throw exception?
                return;
            }

            if(arg.equalsIgnoreCase("discord")) {

                DiscordBot bot = botService.getBot();

                Optional<String> discordId = authPlayer.map(AuthPlayer::getDiscordID);
                if (!discordId.isPresent()) return;

                Optional<User> user = Optional.ofNullable(bot.getRelevantGuild().getMemberById(discordId.get())).map(Member::getUser);
                if (!user.isPresent()) {
                    //TODO: throw exception?
                    return;
                }

                String rndcode = new SecureRandom().nextInt() + "000BB";
                requestCodes.put(playerName, rndcode);

                user.get().openPrivateChannel().complete().sendMessage("Введите эту команду в чате майнкрафта: `/cp confirm "+ rndcode +"`, чтобы подтвердить личность.").queue();

                player.sendMessage(new TextComponent("Вам требуется подтвердить свою личность чтобы продолжить. Бот написал вам в лс Дискорда. Выполните требования бота, чтобы сменить пароль."));

            }else{
                player.sendMessage(new TextComponent("Неправильно введена команда."));
                throw new InvalidCommandArgument();
            }

        }

        List<String> confirmedIdentityPlayerNames = new ArrayList<>();

        @Subcommand("confirm")
        @Syntax("<code>")
        public void onChangePasswordConfirm(@NotNull ProxiedPlayer player, @Single String enteredCode){
            String playerName = player.getName();

            if(confirmedIdentityPlayerNames.contains(playerName)){
                player.sendMessage(new TextComponent("Вы уже подтвердили свою личность. Введите команду /cp <password> <password>, если вам требуется сменить пароль."));
            }

            if(!requestCodes.containsKey(playerName)){
                player.sendMessage(new TextComponent("Вы не создали запрос на смену пароля. Введите команду /cp request discord"));
                return;
            }

            String actualCode = requestCodes.get(playerName);

            if(!enteredCode.equals(actualCode)){
                player.sendMessage(new TextComponent("Код не совпадает с актуальным. Повторите ввод команды."));
                throw new InvalidCommandArgument();
            }

            confirmedIdentityPlayerNames.add(playerName);
            requestCodes.remove(playerName);

            player.sendMessage(new TextComponent("Личность подтверждена. Теперь вы можете сменить пароль. Введите команду /cp <password> <password>"));
        }

        @HelpCommand
        public void onHelp(CommandSender sender, @NotNull CommandHelp help){
            help.showHelp();
        }


    }

    @CommandAlias("discordauth|dauth")
    @CommandPermission("discordauth.admin")
    public static class PluginCommands extends BaseCommand{

        @Inject
        private DataSource db;

        @Inject
        private ProxyServer proxy;

        @Default
        public void onDefault(){
            throw new InvalidCommandArgument();
        }

        @HelpCommand
        public void onHelp(CommandSender sender, @NotNull CommandHelp help){
            help.showHelp();
        }

//        @Subcommand("migrate")
//        @Syntax("")
//        //@CommandPermission("discordauth.admin")
//        public void onMigrate(CommandSender sender, String[] args){
//
//        }



        @Subcommand("player get")
        @CommandCompletion("@allplayers")
        public void onGetPlayer(CommandSender sender, @NotNull @Single String  playerName){
            AuthPlayer authPlayer = db.getPlayer(playerName);
            if(authPlayer == null){
                sender.sendMessage(new TextComponent("Player \"" + playerName +"\" is not in database."));
                return;
            }
            sender.sendMessage(new TextComponent(authPlayer.toString()));
        }

        @Subcommand("player delete")
        @CommandCompletion("@allplayers")
        public void onDeletePlayer(@NotNull CommandSender sender, @NotNull @Single String playerName){
            db.deletePlayer(playerName);
            ProxiedPlayer player = proxy.getPlayer(playerName);
            if(player != null && player.isConnected()){
                Connection.kick(player, "Deleted from database.");
            }
            sender.sendMessage(new TextComponent("Player with name \"" + playerName + "\" was deleted from database."));
        }

//        @Subcommand("change player")
//        @CommandCompletion("@players")
//        public void onChangePlayer(CommandSender sender, @NotNull ProxiedPlayer player){
//
//        }

    }

}
