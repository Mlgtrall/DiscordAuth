package ru.mlgtrall.discordauth.io.database;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import ru.mlgtrall.discordauth.io.log.ConsoleLoggerFactory;
import ru.mlgtrall.discordauth.settings.Settings;
import ru.mlgtrall.discordauth.settings.holders.DataSourceSettings;

public abstract class AbstractSQLDatabase implements DataSource {

    protected final ConsoleLogger log = ConsoleLoggerFactory.get(this.getClass());

    protected String user;

    protected String password;

    protected String dbName;

    protected String tableName;

    protected String host;

    protected String port;

    //TODO: do more abstract?
    protected AbstractSQLDatabase(@NotNull Settings settings){
        this.user = settings.getProperty(DataSourceSettings.MySQL.USER);
        this.password = settings.getProperty(DataSourceSettings.MySQL.PASSWORD);
        this.dbName = settings.getProperty(DataSourceSettings.MySQL.DATABASE_NAME);
        this.tableName = settings.getProperty(DataSourceSettings.MySQL.TABLE);
        this.host = settings.getProperty(DataSourceSettings.MySQL.HOST);
        this.port = String.valueOf(settings.getProperty(DataSourceSettings.MySQL.PORT));


    }
}
