package ru.mlgtrall.jda_bot_bungee_auth.io.database;

import co.aikar.idb.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee_auth.data.AuthPlayer;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.DataColumns;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.KeyHolder;
import ru.mlgtrall.jda_bot_bungee_auth.settings.Settings;
import ru.mlgtrall.jda_bot_bungee_auth.util.PlayerUtil;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.DataColumns.*;
import static ru.mlgtrall.jda_bot_bungee_auth.util.KeyUtil.*;
import static ru.mlgtrall.jda_bot_bungee_auth.util.PlayerUtil.*;
import static ru.mlgtrall.jda_bot_bungee_auth.util.StringUtil.*;
import static ru.mlgtrall.jda_bot_bungee_auth.util.MapUtil.*;

public class MySQLDatabase extends AbstractSQLDatabase {

    Database db;

    public MySQLDatabase(Settings settings, Logger log) throws SQLException {
        super(settings, log);
        DatabaseOptions options = DatabaseOptions
                .builder()
                .mysql(user,password, dbName, host + ":" + port)
                .logger(log)
                .build();

        Database database = PooledDatabaseOptions
                .builder()
                .options(options)
                .dataSourceProperties(new HashMap<String, Object>(){
                    {
                        //Use useSSL=false and allowPublicKeyRetrieval=true together
                        put("useSSL", false);
                        put("allowPublicKeyRetrieval", true);
                    }}).createHikariDatabase();
        DB.setGlobalDatabase(database);
        this.db = database;
        try {
            checkTablesAndColumns();
        } catch (SQLException e) {
            log.warning("Fatal error! Can't initialize the MySQL database:");
            e.printStackTrace();
            log.warning("Please check your database settings in settings.yml file!");
            throw e;
        }
    }

    private void checkTablesAndColumns() throws SQLException{

        //TODO: Make more configurable and more accurate types?
        //TODO: make password_hash unique?
        //TODO: is it dangerous to make mine_uuid unique for pirate players?
        //TODO: resolve discord_id max length
        log.info("Checking tables in \"" + dbName + "\" database");
        log.info("Checking \"" + tableName + "\" table");
        db.executeUpdate(
                "create table if not exists " + tableName + " ("
                        + MINE_NAME.mysql() + " VARCHAR(255) NOT NULL, "
                        + MINE_UUID.mysql() + " VARCHAR(36), "
                        + DISCORD_ID.mysql() + " VARCHAR(64), "
                        + PASSWD_HASH.mysql() + " VARCHAR(255), "
                        + SALT.mysql() + " VARCHAR(255), "
                        + REG_DATE.mysql() + " VARCHAR(64), "
                        + REG_IP.mysql() + " VARCHAR(128), "
                        + LAST_DATE.mysql() + " VARCHAR(64), "
                        + LAST_IP.mysql() + " VARCHAR(128), "
                        + COUNTRY.mysql() + " VARCHAR(64), "
                        + "UNIQUE("+ commaDelimited(MINE_NAME.mysql(), DISCORD_ID.mysql()) +")"
                        + ") CHARACTER SET = utf8;"
        );

        //Checking columns separately
        DatabaseMetaData md = db.getConnection().getMetaData();
        if(isColumnMissing(md, MINE_NAME.mysql())){
            db.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + MINE_NAME.mysql() + " VARCHAR(255) NOT NULL UNIQUE;");
        }

        if(isColumnMissing(md, MINE_UUID.mysql())){
            db.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + MINE_UUID.mysql() + " VARCHAR(36);");
        }

        if(isColumnMissing(md, DISCORD_ID.mysql())){
            db.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + DISCORD_ID.mysql() + " VARCHAR(64);" );
        }

        if(isColumnMissing(md, PASSWD_HASH.mysql())){
            db.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + PASSWD_HASH.mysql() + " VARCHAR(255);");
        }

        if(isColumnMissing(md, SALT.mysql())){
            db.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + SALT.mysql() + " VARCHAR(255);");
        }

        if(isColumnMissing(md, LAST_IP.mysql())){
            db.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + LAST_IP.mysql() + " VARCHAR(128);");
        }

        if(isColumnMissing(md, LAST_DATE.mysql())){
            db.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + LAST_DATE.mysql() + " VARCHAR(64);");
        }

        if(isColumnMissing(md, REG_IP.mysql())){
            db.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + REG_IP.mysql() + " VARCHAR(128);");
        }

        if(isColumnMissing(md, REG_DATE.mysql())){
            db.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + REG_DATE.mysql() + " VARCHAR(64);" );
        }

        if(isColumnMissing(md, COUNTRY.mysql())){
            db.executeUpdate("ALTER TABLE " + tableName + " ADD COLUMN " + COUNTRY.mysql() + " VARCHAR(64);");
        }
        log.info("Checking tables done successfully!");
    }

    private boolean isColumnMissing(DatabaseMetaData md, String columnName) throws SQLException {
        try(ResultSet rs = md.getColumns(null, null, tableName, columnName)){
            return !rs.next();
        }
    }

    @Override
    public boolean isCached() {
        return false;
    }

    @Override
    public AuthPlayer getPlayer(@NotNull String name) {
        DbRow row = null;
        try {
            row = db.getFirstRow( "select * from "+ tableName +" where "+ MINE_NAME.mysql() +" = ?;", name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return PlayerUtil.newPlayerFromMap(row);
    }

    @Override
    public @Nullable List<AuthPlayer> getAllPlayers() {
        List<DbRow> rows = null;
        try {
            rows = db.getResults("select * from " + tableName + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return PlayerUtil.newPlayersFromMaps(rows);
    }

    @Override
    public @Nullable List<AuthPlayer> getAllPlayersByIp(@NotNull String ip) {
        List<AuthPlayer> players = null;
        try {
            List<DbRow> rows = db.getResults("select * from " + tableName + " where "+ LAST_IP.mysql() + " = ?;", ip);
            players = newPlayersFromMaps(rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    @Override
    public boolean updatePlayer(@NotNull String key, @NotNull String data, @NotNull String playerName) {
        KeyHolder validKey = DataColumns.isValidKey(key);
        if(validKey == null){
            return false;
        }
        return updatePlayer(validKey, data, playerName);
    }

    @Override
    public boolean updatePlayer(@NotNull KeyHolder key, @NotNull String data, @NotNull String playerName) {
        int result = 0;
        try {
            result = db.executeUpdate("update ? set ? = ? where ? = ?;",
                    tableName,
                    key.getContextualKey().toString(),
                    data, 
                    MINE_NAME.getContextualKey().toString(),
                    playerName
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result != 0;
    }

    @Override
    public boolean savePlayer(@NotNull AuthPlayer player) {
        Map<KeyHolder, String> playerData = parseToMap(player);
        String wrappedKeys = wrapKeys(DataColumns.values(), ",", null, null);
        String wrappedValues = wrapStringArray(getValuesList(playerData), "','", "'", "'");
        String wrappedDataForUpdate = asStringPairedSequence(wrapMapValues(playerData, "'", "'"), "=", ",", null, null);
        Long result = null;
        try {
            result = db.executeInsert("insert into " + tableName + " (" + wrappedKeys +") values(" + wrappedValues + ") " +
                            "on duplicate key update "+ wrappedDataForUpdate + ";"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result != null;
    }

    @Override
    public boolean deletePlayer(@NotNull String name) {
        int result = 0;
        try {
            result = db.executeUpdate("delete from " + tableName + " where "+MINE_NAME.mysql()+" = ?;",
                    name
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result != 0;
    }

    @Override
    public boolean containsPlayer(@NotNull String playerName){
        return getPlayer(playerName) != null;
    }

    @Override
    public boolean containsPlayer(@NotNull KeyHolder key, @NotNull AuthPlayer player) {
        Map<KeyHolder, String> playerData = parseToMap(player);
        DbRow row = null;
        try {
            row = db.getFirstRow("select * from " + tableName + " where " + MINE_NAME.mysql() + " = ? and "+ key.getContextualKey().toString() +" != null;",
                    player.getName()
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row != null;
    }

    @Override
    public void reload() {

    }
}
