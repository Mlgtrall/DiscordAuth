package ru.mlgtrall.jda_bot_bungee.io.database;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.bungee.util.ChatManager;
import ru.mlgtrall.jda_bot_bungee.io.ConfigFiles;
import ru.mlgtrall.jda_bot_bungee.io.FileLoader;
import ru.mlgtrall.jda_bot_bungee.io.config.ConfigFile;
import ru.mlgtrall.jda_bot_bungee.io.transfer.MySQLDriver;
import ru.mlgtrall.jda_bot_bungee.security.Hash;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class PlayerDatabase {

    private static final Main pl = Main.getInstance();

    public PlayerDatabase(){
        
    }

    public static @Nullable HashMap<String,String> getPlayerData(@NotNull Configuration config, String name){
        LinkedHashSet<String> keys = (LinkedHashSet<String>) config.getKeys();
        String root = name + ".";
        HashMap<String, String > playerData = new HashMap<>();

        boolean playerDefined = false;
        for (String key : keys) {
            if (key.equals(name)) {
                if(!config.contains(root + "COUNTRY")) return null;
                playerData.put("MINE_NAME", key);
                playerDefined = true;
            }
            if(playerDefined) {

                playerData.put("REG_DATE", config.getString(root + "REG_DATE"));
                playerData.put("MINE_UUID", config.getString(root + "MINE_UUID"));
                playerData.put("DISCORD_ID", config.getString(root + "DISCORD_ID"));
                playerData.put("PASSWORD", config.getString(root + "PASSWORD"));
                config.getSection(root + "REG_IP").getKeys().forEach(k ->
                        playerData.put("REG_IP", k));
                return playerData;

            }
        }
        return null;
    }


    public static @NotNull HashMap<String, String> getPlayerData(String mineName){
        HashMap<String, String> playerData = new HashMap<>();
        Connection connection;
        try {
            connection = MySQLDriver.getConnection();
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM " + MySQLDriver.table + " WHERE " + SQLKeys.MINE_NAME.toString() + " " + mineName;
            ResultSet resultSet = statement.executeQuery(sql);
            connection.close();
            for(SQLKeys key : SQLKeys.values()){
                if(key.equals(SQLKeys.MINE_NAME)){
                    playerData.put(key.toString(), mineName);
                }else {
                    playerData.put(key.toString(), resultSet.getString(resultSet.findColumn(key.toString())));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return playerData;
    }


    public static HashMap<String, String> toMySQLFormat(@NotNull HashMap<String, String> playerData){
        String password = playerData.get("PASSWORD");
        String salt = Hash.createSaltStr();
        String hash = Hash.generateHash(password, salt);
        playerData.remove("PASSWORD");
        playerData.put("PASSWD_HASH", hash);
        playerData.put("SALT", salt);
        playerData.put("REG_IP", playerData.get("REG_IP").split("/")[1].replaceAll("_",".")  );
        return playerData;
    }


    public static void checkForFullReg(@NotNull ConfigFile configFile){
        Configuration config = configFile.getConfig();
        for(String name : getPlayerNames(config)){
            if(getPlayerData(config, name) == null){
                config.set(name, null);
            }
        }
        configFile.save();
    }

    public static @NotNull ArrayList<String> getPlayerNames(@NotNull Configuration config){
        LinkedHashSet<String> keys = (LinkedHashSet<String>) config.getKeys();
        ArrayList<String> playerNames = new ArrayList<>(keys);
        return playerNames;
    }


    public static boolean isPlayerName(@NotNull String key){
//        for(YMLKeys ymlKey : YMLKeys.values()){
//            if(ymlKey.toString().equals(key)){
//                return false;
//            }
//        }
//        return true;
        return !key.equalsIgnoreCase("REG_DATE")
                && !key.equalsIgnoreCase("MINE_UUID")
                && !key.equalsIgnoreCase("DISCORD_ID")
                && !key.equalsIgnoreCase("PASSWORD")
                && !key.equalsIgnoreCase("REG_IP")
                && !key.equalsIgnoreCase("COUNTRY")
                && !key.equalsIgnoreCase("LAST_DISPLAY_NAME")
                && !key.equalsIgnoreCase("LOGIN_IP");
    }

    public static boolean check(@NotNull ProxiedPlayer player){
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
