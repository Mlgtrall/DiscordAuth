package ru.mlgtrall.jda_bot_bungee;

import ru.mlgtrall.jda_bot_bungee.bungee.command.AuthCommand;
import ru.mlgtrall.jda_bot_bungee.bungee.command.LoginCommand;
import ru.mlgtrall.jda_bot_bungee.bungee.command.RegisterCommand;
import ru.mlgtrall.jda_bot_bungee.bungee.listener.*;
import ru.mlgtrall.jda_bot_bungee.io.ConfigFiles;
import ru.mlgtrall.jda_bot_bungee.io.FileLoader;
import ru.mlgtrall.jda_bot_bungee.io.file.config.YMLConfigFile;
import ru.mlgtrall.jda_bot_bungee.jda.BotFactory;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.util.*;


public final class Main extends Plugin {

    //instances
    private static Main instance;
    private static BotFactory botFactory;

    private static HashMap<String,String> nameCodeMap;
    private static HashMap<String,Integer> nameTaskIdList;
    private static HashMap<String,String> nameMineIdDiscordMap;

    private Set<UUID> verifiedmembers; //UUID (Minecraft) of logged players

    private FileLoader fileLoader;

    public static Main getInstance() {
        return instance;
    }

    public static void setInstance(Main instance) {
        Main.instance = instance;
    }

    @Override
    public void onEnable() {
        setInstance(this);
        getLogger().info("Plugin enabled! | by Mlgtrall");
        build();

        getLogger().info("All done!");
        getLogger().info("Plugin is fully operable!");

    }


    private void assembleBot(){
        //Loading bot
        botFactory = BotFactory.getInstance(); //test
    }

    @Override
    public void onDisable() {
        getLogger().info("plugin disabled! | By Mlgtrall");
    }

    private void build(){
        getLogger().info("Assembling public vars and etc...");
        nameTaskIdList = new HashMap<>();
        nameCodeMap = new HashMap<>();
        nameMineIdDiscordMap = new HashMap<>();
        verifiedmembers = new HashSet<>();
        getLogger().info("Done!");

        dependencies();
        registerUtils();
        assembleBot();
        registerListeners();
        registerCommands();
        checkDB();
    }

    private void dependencies() {
        getLogger().info("Loading dependencies...");
        if(getProxy().getPluginManager().getPlugin("LiteBans") == null){
            getLogger().warning("LiteBans plugin not found!");
        }
        getLogger().info("Loading dependencies done!");

    }

    public static void main(String[] args) {
    }

    private void checkDB() {

        YMLConfigFile dbConfigFile = fileLoader.get(ConfigFiles.PLAYER_DB_YML);
        Configuration playerDB = dbConfigFile.getConfig();


//        getProxy().getScheduler().schedule(this, () -> {
//                ArrayList<String> playerNamesList = DatabaseProvider.getPlayerNames(playerDB);
//                for (String name : playerNamesList){
//                    HashMap<String, String> playerData = DatabaseProvider.getPlayerData(playerDB, name);
//                    assert playerData != null;
//                    String id = playerData.get("DISCORD_ID");
//                    botFactory.getGuild().getMember(botFactory
//                            .getJDA().getUserById(id)).getRoles().containsAll(botFactory.getRequiredRoleList());
//                    JDA jda = botFactory.getJDA();
//                    Guild guild = botFactory.getGuild();
//                    User user = jda.getUserById(id);
//                    Member member = guild.getMember(user);
//                    List<Role> roles = member.getRoles();
//                    roles.forEach(role -> {
//                        String roleId = role.getId();
//                        String roleName = role.getName();
//                        List<String> requiredRoles = botFactory.getRequiredRoleList();
//                        if(requiredRoles.contains(roleId) || requiredRoles.contains(roleName)){
//                            MySQLDriver.update(id, "VERIFIED", "1");
//                        }else{
//                            MySQLDriver.update(id, "VERIFIED", "0");
//                        }
//                    });
//                }
//
//        }, 5, TimeUnit.MINUTES); //add to bot?

    }

    private void registerListeners(){
        getLogger().info("Registering bungee listeners...");
        //Event listeners bungee
        getProxy().getPluginManager().registerListener(this, new PostLoginEventListener(this));
        getProxy().getPluginManager().registerListener(this, new PlayerDisconnectListener(this));
        getProxy().getPluginManager().registerListener(this, new ChatListener(this));
        getProxy().getPluginManager().registerListener(this, new ServerConnectEventListener(this));
        getProxy().getPluginManager().registerListener(this, new ServerConnectedListener(this));
        getProxy().getPluginManager().registerListener(this, new ServerSwitchEventListener(this));
        getLogger().info("Registering bungee listeners done!");
    }

    private void registerCommands(){
        getLogger().info("Registering bungee commands...");

        //Registering commands bungee
        getProxy().getPluginManager().registerCommand(this, new AuthCommand(this));
        getProxy().getPluginManager().registerCommand(this, new RegisterCommand(this));
        getProxy().getPluginManager().registerCommand(this, new LoginCommand(this));
        getLogger().info("Registering bungee commands done!");
    }

    private void registerUtils(){
        getLogger().info("Registering util instances...");
        fileLoader = FileLoader.getInstance();
        getLogger().info("Registering util instances done!");
    }


    public HashMap<String, Integer> getNameTaskIdList() {
        return nameTaskIdList;
    }

    public HashMap<String, String> getNameCodeMap() {
        return nameCodeMap;
    }

    public HashMap<String, String> getNameMineIdDiscordMap() {
        return nameMineIdDiscordMap;
    }

    public FileLoader getFileLoader() {
        return fileLoader;
    }

    public Set<UUID> getVerifiedMembers() {
        return verifiedmembers;
    }

    public BotFactory getBotAssembler() {
        return botFactory;
    }

}
