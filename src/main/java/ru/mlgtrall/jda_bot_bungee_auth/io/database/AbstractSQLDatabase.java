package ru.mlgtrall.jda_bot_bungee_auth.io.database;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.settings.Settings;
import ru.mlgtrall.jda_bot_bungee_auth.settings.holders.DataSourceSettings;

import java.util.logging.Logger;

public abstract class AbstractSQLDatabase implements DataSource {

    protected Logger log;

    protected String user;

    protected String password;

    protected String dbName;

    protected String tableName;

    protected String host;

    protected String port;

    //TODO: do more abstract
    protected AbstractSQLDatabase(@NotNull Settings settings, @NotNull Logger log){
        this.log = log;
        this.user = settings.getProperty(DataSourceSettings.MySQL.USER);
        this.password = settings.getProperty(DataSourceSettings.MySQL.PASSWORD);
        this.dbName = settings.getProperty(DataSourceSettings.MySQL.DATABASE_NAME);
        this.tableName = settings.getProperty(DataSourceSettings.MySQL.TABLE);
        this.host = settings.getProperty(DataSourceSettings.MySQL.HOST);
        this.port = String.valueOf(settings.getProperty(DataSourceSettings.MySQL.PORT));

    }
}
