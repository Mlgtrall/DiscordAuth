package ru.mlgtrall.discordauth.bootstrap;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.io.database.AbstractSQLDatabase;
import ru.mlgtrall.discordauth.io.database.CacheDataSource;
import ru.mlgtrall.discordauth.io.database.DataSource;
import ru.mlgtrall.discordauth.io.database.MySQLDatabase;
import ru.mlgtrall.discordauth.io.log.ConsoleLogger;
import ru.mlgtrall.discordauth.io.log.ConsoleLoggerFactory;
import ru.mlgtrall.discordauth.settings.Settings;
import ru.mlgtrall.discordauth.settings.holders.DataSourceSettings;

import javax.inject.Inject;
import javax.inject.Provider;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourceProvider implements Provider<DataSource> {

    //TODO: use or not?
    private final Map<Class<? extends AbstractSQLDatabase>, DataSource> providers = new ConcurrentHashMap<>();

    @Inject
    private Settings settings;

    private final ConsoleLogger log = ConsoleLoggerFactory.get(this.getClass());

    //TODO: final?
    private DataSource currentSource;

    private @NotNull DataSource defineGlobalDataSourceFromConfig(){

        log.info("Initializing global data source from settings...");
        //TODO: add bean property
        String dataSource = settings.getProperty(DataSourceSettings.BACKEND);

        DataSource actualSource = null;

        if(dataSource.trim().equalsIgnoreCase("mysql")){
            try {
                try {
                    providers.put(MySQLDatabase.class, new MySQLDatabase(settings));
                } catch (SQLException e) {
                    log.warning("Could not create data source:");
                    e.printStackTrace();
                    throw new IllegalStateException("Error during init of data source.");
                }
                actualSource = providers.get(MySQLDatabase.class);
            }catch (NoClassDefFoundError e){
                e.printStackTrace();
            }
            if(actualSource == null){
                throw new NullPointerException("MySQLDatabase class not found.");
            }
        }else{
            //Database by default
            log.info("No required database specification was found. Setting MySQL by default.");
            actualSource = providers.get(MySQLDatabase.class);

            if(actualSource == null){
                throw new NullPointerException("MySQLDatabase class not found! Aborting loading.");
            }
        }

        if(settings.getProperty(DataSourceSettings.USE_CACHING)){
            actualSource = new CacheDataSource(actualSource);
        }

        return actualSource;
    }

    public DataSource getProvider(Class<? extends AbstractSQLDatabase> provider){
        return providers.get(provider);
    }

    @Override
    public DataSource get() {
        try {
            if (currentSource != null) {
                return currentSource;
            } else {
                return currentSource = defineGlobalDataSourceFromConfig();
            }
        }catch (Exception e){
            log.warning("Could not create data source:");
            e.printStackTrace();
            throw new IllegalStateException("Error during initialization of data source");
        }
    }
}
