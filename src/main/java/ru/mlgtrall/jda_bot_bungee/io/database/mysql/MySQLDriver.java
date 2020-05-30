package ru.mlgtrall.jda_bot_bungee.io.database.mysql;

import ch.jalu.configme.SettingsManager;
import javafx.util.Pair;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee.settings.Settings;
import ru.mlgtrall.jda_bot_bungee.settings.holders.DataSourceSettingsHolder;
import ru.mlgtrall.jda_bot_bungee.io.database.DataColumns;
import ru.mlgtrall.jda_bot_bungee.io.database.KeyHolder;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static ru.mlgtrall.jda_bot_bungee.util.StringUtil.wrapValues;

public class MySQLDriver {

    private static MySQLDriver instance;

    private static final Settings SETTINGS = Settings.getInstance();

    private static final SettingsManager settingsManager = SETTINGS.getSettingsManager();

    public static MySQLDriver getInstance() {
        return instance == null ? instance = new MySQLDriver() : instance;
    }

    @Getter
    private Connection connection;

    private MySQLDriver(){
        establishConnection();
    }

    public void establishConnection(){
        if(this.connection != null) return;
        try {
            this.connection = DriverManager.getConnection(url(), user(), password());
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        if(this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                this.connection = null;
            }
        }
    }

    private String getTable(){
        return settingsManager.getProperty(DataSourceSettingsHolder.MySQL.TABLE);
    }

    private @NotNull String url(){
        return "jdbc:mysql://" + settingsManager.getProperty(DataSourceSettingsHolder.MySQL.HOST) + ":" + settingsManager.getProperty(DataSourceSettingsHolder.MySQL.PORT) + "/" +
                settingsManager.getProperty(DataSourceSettingsHolder.MySQL.DATABASE) + "?autoReconnect=true&useSSL=false";
    }

    private @NotNull String user() {
        return settingsManager.getProperty(DataSourceSettingsHolder.MySQL.USER);
    }

    private @NotNull String password(){
        return settingsManager.getProperty(DataSourceSettingsHolder.MySQL.USER);
    }


    public void insert(@NotNull Map<KeyHolder, String> data){

        final Pair<String, String> wrappedData = wrapValues(data.entrySet());
        final String sql = "INSERT INTO " + getTable() + " " + wrappedData.getKey() + " VALUES" + wrappedData.getValue();
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    public boolean containsRow(@NotNull String columnName, @NotNull String data){
        final String sql = "SELECT * FROM " + getTable() + " WHERE " + columnName + " = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)){
            statement.setString(1, data);
            try(ResultSet resultSet = statement.executeQuery(sql)) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void update(String keyToChange, String data, String columnName, String columnData){
        final String sql = "UPDATE " + getTable() + " SET " + keyToChange + "= ? WHERE " + columnName + " = ?";
        try(PreparedStatement preparedStatement = this.connection.prepareStatement(sql)){
            preparedStatement.setString(1, data);
            preparedStatement.setString(2, columnData);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRow(String columnName, String data){
        final String sql = "DELETE from " + getTable() + " WHERE " + columnName + " = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, data);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public @Nullable Map<String, String> queryRow(String columnName, String data){
        Map<String, String> map = new HashMap<>();
        final String sql = "SELECT * FROM " + getTable() + " WHERE " + columnName + " = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, data);
            try(ResultSet resultSet = statement.executeQuery(sql)){
                for(DataColumns column : DataColumns.values()){
                   map.put(column.getName(), resultSet.getString(resultSet.findColumn(column.getName())));
                }
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




}
