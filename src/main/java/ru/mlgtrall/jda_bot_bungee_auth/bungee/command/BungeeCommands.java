package ru.mlgtrall.jda_bot_bungee_auth.bungee.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.annotation.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.tjplaysnow.discord.object.logger.LogLevel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.InjectorContainer;
import ru.mlgtrall.jda_bot_bungee_auth.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee_auth.discord.DiscordBot;
import ru.mlgtrall.jda_bot_bungee_auth.discord.DiscordBotService;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.DataSource;
import ru.mlgtrall.jda_bot_bungee_auth.security.Hash;
import ru.mlgtrall.jda_bot_bungee_auth.security.HashedPassword;
import ru.mlgtrall.jda_bot_bungee_auth.security.Password;
import ru.mlgtrall.jda_bot_bungee_auth.util.TitleManager;

import javax.inject.Inject;
import java.security.SecureRandom;
import java.util.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BungeeCommands {

    @Inject
    private DiscordBotService botService;

    @Inject
    private DataSource db;

    @Inject
    private Logger log;

    @Inject
    private TaskScheduler scheduler;

    @Inject
    private Main pl;

    @CommandAlias("authme")
    public static class AuthMeCommand extends BaseCommand{

        @Default
        public void onAuthMe(ProxiedPlayer player, String[] args){

        }

        @HelpCommand
        public void doHelp(CommandSender sender, @NotNull CommandHelp help) {
            help.showHelp();
        }
    }

    @CommandAlias("changepassword|changepass|cp")
    public static class ChangePasswordCommand extends BaseCommand{

        @Inject
        private DiscordBotService botService;

        @Inject
        private DataSource db;

        @Inject
        private Logger log;

        @Inject
        private TaskScheduler scheduler;

        @Inject
        private Main pl;

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

            log.info("Password was changed for player " + playerName + " from " + oldHashedPassword + " to " + hashedPassword);

            scheduler.schedule(pl, () -> player.sendMessage(new TextComponent("Пароль успешно изменен.")), 1, TimeUnit.SECONDS);
            scheduler.schedule(pl, () -> confirmedIdentityPlayerNames.remove(playerName), 20, TimeUnit.MINUTES);
        }

        //Map to store [playerName,hisRequestCode]
        private final Map<String, String> requestCodes = new ConcurrentHashMap<>();

        @Subcommand("request")
        @Description("A command to request a password change mission.")
        @Syntax("<discord>")
        public void onChangePasswordRequest(@NotNull ProxiedPlayer player, @Single String arg){
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
//
//    @CommandAlias("auth")
//    public static class PluginCommands extends BaseCommand{
//
//
//
//        @HelpCommand
//        public void onHelp(CommandSender sender, @NotNull CommandHelp help){
//            help.showHelp();
//        }
//
//        public static class AdminCommands extends BaseCommand{
//
//        }
//
//        @Subcommand("admin|adm")
//        //@CommandPermission("discordauth.admin")
//        public void onAdminCommand(){
//
//
//        }
//
//        @Subcommand("admin|adm migrate")
//        @Syntax("")
//        //@CommandPermission("discordauth.admin")
//        public void onMigrate(CommandSender sender, String[] args){
//
//        }
//
//        @Subcommand("admin|adm player")
//        public void onPlayer(){
//
//        }
//
//    }

}
