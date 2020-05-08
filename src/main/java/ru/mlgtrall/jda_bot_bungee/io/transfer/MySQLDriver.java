package ru.mlgtrall.jda_bot_bungee.io.transfer;

import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class MySQLDriver {
    private static final Main pl = Main.getInstance();
    public static final String db = "MCFP";
    public static final String table = "main";
    private static final String user = "mcfp";
    private static final String password = "mcfploveagain";
    private static final String primaryKey = "DISCORD_ID";
    private static final String ip = "localhost";
    private static final String url = "jdbc:mysql://"+ip+":3306/" + db + "?useSSL=false&autoReconnect=true&maxReconnects=10";


    public MySQLDriver(@NotNull Configuration config){
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

        public static void insert(@NotNull HashMap<String, String> playerData){
        try{
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            String[] data = new String[playerData.size()];
            int i = 0;
            for (String key : playerData.keySet()){
                data[i] = playerData.get(key);
                i++;
            }
            String[] keySet = new String[playerData.size()];
            playerData.keySet().toArray(keySet);
            String sql = buildInsertSQL(table, keySet, data);
            pl.getLogger().info("SQL = " + sql);
            statement.executeUpdate(sql);
            connection.close();
        }catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public static void delete(){

    }

    public static void update(String key, String what, String newData){
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement statement = conn.createStatement();
            String sql = "UPDATE " + table + " SET " + what + " = '" + newData + "' WHERE "
                    + primaryKey + " = '" +key+ "'";
            statement.executeUpdate(sql);
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void update(String key, String[] what, String[] newData){

    }

    public static void insert(final String @NotNull [] columns, final String[] data){
        final String url = "jdbc:mysql://" + ip + ":3306/" + db + "?user="+ user +"&password="+ password + "&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&failOverReadOnly=false&maxReconnects=10";
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            String sql = buildInsertSQL(table, columns, data);

            statement.executeUpdate(sql);
            connection.close();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

//    private static String buildInsertSQL(HashMap<String ,String > hashMap, String table){
//        StringBuilder sql = new StringBuilder("insert into " + table + " (");
//        for(String col: )
//
//    }

    private static @NotNull String buildInsertSQL(final String table, final String @NotNull [] columns, final String[] data){
        StringBuilder sql = new StringBuilder("INSERT INTO " + table + " (");
        pl.getLogger().info("Columns:");
        for(String col : columns){
            pl.getLogger().info(col);
            if(!col.isEmpty()) {
                sql.append(col).append(",");
            }
        }
        sql.deleteCharAt(sql.lastIndexOf(","));
        sql.append(") ").append("VALUES('");
        pl.getLogger().info("Data:");
        for(String val : data){
            pl.getLogger().info(val);
            if(!val.isEmpty()) {
                sql.append(val).append("','");
            }
        }
        sql.delete(sql.lastIndexOf(","), sql.lastIndexOf(",") + 2);
        sql.append("')");
        sql.deleteCharAt(sql.lastIndexOf(")")-1);

        return sql.toString();
    }
}
