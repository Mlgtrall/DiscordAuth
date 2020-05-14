package ru.mlgtrall.jda_bot_bungee.io.database;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.bungee.util.ChatManager;
import ru.mlgtrall.jda_bot_bungee.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee.io.ConfigFiles;
import ru.mlgtrall.jda_bot_bungee.io.FileLoader;
import ru.mlgtrall.jda_bot_bungee.io.file.config.YMLConfigFile;
import ru.mlgtrall.jda_bot_bungee.security.Hash;

import java.util.*;


public abstract class DatabaseProvider {

//      private static final DatabaseProvider instance;
    private static final Main pl = Main.getInstance();
    private static final Map<Class<? extends DatabaseProvider>, DatabaseProvider> providers = new HashMap<>();

//    public static DatabaseProvider getInstance() {
//        return instance;
//    }

    protected DatabaseProvider(){}

    static
    {
//        instance = new DatabaseProvider();
        try{
            providers.put(SQLDatabase.class, new SQLDatabase());
        }catch (NoClassDefFoundError ignored){}

        try {
            providers.put(YMLDatabase.class, new YMLDatabase());
        }catch (NoClassDefFoundError ignored){}

    }

    public static DatabaseProvider getProvider(Class<? extends DatabaseProvider> provider){
        return providers.get(provider);
    }


    public abstract AuthPlayer getPlayer();





    public static @Nullable HashMap<String,String> getPlayerData(@NotNull Configuration config, String name){
        LinkedHashSet<String> players = (LinkedHashSet<String>) config.getKeys();

        boolean playerDefined = false;
        for(String player : players){
            if (player.equals(name)) {
                playerDefined = true;
                break;
            }
        }

        if(!playerDefined){
            pl.getLogger().warning("The player, declared as '"+ name +"' doesn't exist in database.");
            return null;
        }

        String pathToPlayer = YMLKeys.defPathToPlayer + name;
        LinkedHashSet<String> playerDataKeys = (LinkedHashSet<String>) config.getSection(pathToPlayer).getKeys();
        HashMap<String, String> playerData = new HashMap<>();

        for(YMLKeys ymlKey : YMLKeys.values()){
            playerData.put(ymlKey.getName(), null);
        }

        for(String key : playerDataKeys){
            for(YMLKeys ymlKey : YMLKeys.values()){
                if(ymlKey.getName().equals(key)){
                    playerData.put(ymlKey.getName(), config.getString(pathToPlayer + YMLKeys.separator + key ));
                }
            }
        }

        return playerData;
    }

    public static @NotNull Collection<String> getPlayersKeys(Configuration config){
        Collection<String> playersKeys;
        String playersLevel = YMLKeys.defPathToPlayer;
        if(!playersLevel.isEmpty()){
            config = config.getSection(playersLevel);
        }
        playersKeys = config.getKeys();
        return playersKeys;
    }

    public static void hashedPasswordCheck(@NotNull YMLConfigFile playerDBYMLFile){
        Configuration playerYMLDB = playerDBYMLFile.getConfig();
//        LinkedHashSet<String> playersNames = (LinkedHashSet<String>) getPlayersKeys(playerYMLDB);
        LinkedHashSet<String> playersNames = (LinkedHashSet<String>) playerYMLDB.getKeys();

        for(String playerName : playersNames){
            String password = playerYMLDB.getString(YMLKeys.PASSWORD.addBeforePath(playerName).getPath());
            String salt = playerYMLDB.getString(YMLKeys.SALT.addBeforePath(playerName).getPath());
            String hash = playerYMLDB.getString(YMLKeys.SALT.addBeforePath(playerName).getPath());
            if(password != null){
                if(salt == null || salt.isEmpty()){
                    salt = Hash.createSaltStr();
                }

                hash = Hash.generateHash(password, salt);

                playerYMLDB.set(YMLKeys.PASSWORD.addBeforePath(playerName).getPath(), null);
                playerYMLDB.set(YMLKeys.SALT.addBeforePath(playerName).getPath(), salt);
                playerYMLDB.set(YMLKeys.PASSWD_HASH.addBeforePath(playerName).getPath(), hash);
            }else if(salt == null || hash == null || hash.isEmpty() || salt.isEmpty()){
                playerYMLDB.set(YMLKeys.PASSWD_HASH.addBeforePath(playerName).getPath(), null);
                playerYMLDB.set(YMLKeys.SALT.addBeforePath(playerName).getPath(), null);
            }
        }
        playerDBYMLFile.save();

    }

    public static @NotNull Map<String, String> convertYmlToMySQL(@NotNull HashMap<String, String> playerData){
        String salt = playerData.get(YMLKeys.SALT.getName());
        String hash = playerData.get(YMLKeys.PASSWD_HASH.getName());
        playerData.remove("PASSWORD");
        playerData.put("PASSWD_HASH", hash);
        playerData.put("SALT", salt);
        playerData.put("REG_IP", playerData.get("REG_IP").split("/")[1].replaceAll("_",".")  );
        return playerData;
    }


    public static void checkForFullReg(@NotNull YMLConfigFile configFile){ //change!!!
        Configuration config = configFile.getConfig();
        for(String name : getPlayersNames(config)){
            if(getPlayerData(config, name) == null){
                config.set(name, null);
            }
        }
        configFile.save();
    }

    public static @NotNull Map<String, HashMap<String, String>> getAllPlayersData(@NotNull Configuration config){
        HashMap<String, HashMap<String, String>> allPlayersData = new HashMap<>();
         for(String name: getPlayersNames(config)){
             allPlayersData.put(name, getPlayerData(config, name));
         }
         return allPlayersData;
    }

    public static @NotNull Collection<String> getPlayersNames(@NotNull Configuration config){
        LinkedHashSet<String> playersNamesSet = (LinkedHashSet<String>) config.getKeys();
        return new LinkedHashSet<>(playersNamesSet);
    }


    public static boolean isPlayerName(@NotNull String key){
        for(YMLKeys ymlKey : YMLKeys.values()){
            if(key.equals(ymlKey.getName())){
                return false;
            }
        }
        return true;
    }

    public static boolean checkState(@NotNull ProxiedPlayer player){
        Configuration playerDB = FileLoader.getInstance().get(ConfigFiles.PLAYER_DB_YML).getConfig();
        String playerName = player.getName();

        //Check if not passed registration
        if(!playerDB.contains(playerName + ".PASSWORD") && playerDB.contains(playerName + ".DISCORD_ID")){
            player.sendMessage(ChatManager.fromConfig("need_reg", true));
            player.sendMessage(ChatManager.fromConfig("join2"));
            return true;
        //Check if not passed auth
        }else if(!playerDB.contains(playerName + ".DISCORD_ID")){
            player.sendMessage(ChatManager.fromConfig("need_auth", true));
            player.sendMessage(ChatManager.fromConfig("join"));
            return true;
        }
        return false;
    }
}
