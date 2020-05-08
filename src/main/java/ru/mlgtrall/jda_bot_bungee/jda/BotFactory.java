package ru.mlgtrall.jda_bot_bungee.jda;

import com.tjplaysnow.discord.object.Bot;
import com.tjplaysnow.discord.object.CommandConsoleManager;
import com.tjplaysnow.discord.object.ThreadHandle;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.md_5.bungee.config.Configuration;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.io.ConfigFiles;
import ru.mlgtrall.jda_bot_bungee.jda.listener.GuildMessage;
import ru.mlgtrall.jda_bot_bungee.jda.util.JDAConfigUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BotFactory {

    private static final BotFactory instance;
    private JDA jda;
    private Bot bot;
    private static final Main pl = Main.getInstance();

    private Configuration bungeeConfig;
    private Guild guild;
    private static final String prefix = "r!";
    //private final JDAConfigUtils jdaConfigUtils;
    public static final String guildId = "675311796118355968";

    public static final String  botDefChannelId = "682292748900106265";
    public List<String> botChannels;
    public static final String  verifiedRoleId = "675307161936723999";
    public static final String  verifiedSubRoleId = "684340846203764753";

    public static BotFactory getInstance() {
        return instance;
    }

    private void botChannelSettings(){
        botChannels.add(botDefChannelId);
        botChannels.add("675311796118355968"); //bot-settings
    }

    public List<String> getBotChannels() {
        return botChannels;
    }

    private final List<String> requiredRoleList;

    private void requiredRolesSettings(){
        requiredRoleList.add(verifiedRoleId);
        requiredRoleList.add(verifiedSubRoleId);
        requiredRoleList.add("697736720325673040"); //YouTube member
        requiredRoleList.add("697736536774410290"); //Twitch subscriber
        requiredRoleList.add("697736536774410291"); //Twitch subscriber: Tier 1
        requiredRoleList.add("697736536774410292"); //Twitch subscriber: Tier 2
        requiredRoleList.add("697736536774410293"); //Twitch subscriber: Tier 3
        requiredRoleList.add("verified");
        requiredRoleList.add("verifiedSub");
    }

    public List<String> getRequiredRoleList() {
        return requiredRoleList;
    }

    static
    {
        instance = new BotFactory();
    }

    private BotFactory(){
        pl.getLogger().info("Assembling jda bot...");
        bungeeConfig = pl.getFileLoader().get(ConfigFiles.BUNGEE_CHAT).getConfig();
        botChannels = new ArrayList<>();
        requiredRoleList = new ArrayList<>();
        botChannelSettings();
        requiredRolesSettings();
        startBot();

        pl.getProxy().getScheduler().schedule(pl, () -> guild = jda.getGuilds().get(0), 1, TimeUnit.SECONDS); //?
        pl.getLogger().info("Assembling jda bot done!");
    }


    private void startBot() {

        // Starting bot, using Discord API by tjplaysnow
        /*
            Original code for Discord API:
            ---------------------------------------
            bot = new Bot(jdaConfigUtils.fromConfig("token"), prefix);
            bot.setBotThread(new ThreadHandle());
            bot.setConsoleCommandManager(new CommandConsoleManager());
        */

        pl.getLogger().info("Starting jda bot...");
        bot = new Bot(JDAConfigUtils.fromConfig("token"), prefix);
        pl.getLogger().info("Bot has started with prefix = \"" + prefix + "\"");

        pl.getLogger().info("Registering bot thread...");
        bot.setBotThread(new ThreadHandle());
        pl.getLogger().info("Done!");

        pl.getLogger().info("Registering bot command manager...");
        bot.setConsoleCommandManager(new CommandConsoleManager());
        pl.getLogger().info("Done!");
        //bot.addCommand(new HelpCommand(bot)); // ???

        jda = bot.getBot();

        pl.getLogger().info("Registering GuildMessage listener...");
        jda.addEventListener(new GuildMessage(pl,this));
        pl.getLogger().info("Done!");

        //JDA Style starting
        /*try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(jdaUtils.fromConfig("TOKEN"))
                    .addEventListeners(new GuildMessage(plugin,this))
                    .setHttpClient(new OkHttpClient.Builder().build())
                    .build()
                    .awaitReady();

            plugin.getLogger().info("assembling jda bot ok!");
        } catch(LoginException | InterruptedException e) {
            e.printStackTrace();
            plugin.getLogger().info("assebmling jda bot caused an error!");
        }*/
    }

    public Guild getGuild() {
        return guild;
    }

    public JDA getJDA() {
        return bot.getBot();
    }

    public Bot getBot(){
        return bot;
    }
}
