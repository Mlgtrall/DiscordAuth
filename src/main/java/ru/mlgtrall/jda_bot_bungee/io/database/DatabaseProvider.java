package ru.mlgtrall.jda_bot_bungee.io.database;

import ch.jalu.configme.SettingsManager;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.settings.Settings;
import ru.mlgtrall.jda_bot_bungee.settings.holders.DataSourceSettingsHolder;

import java.util.*;
import java.util.logging.Logger;


public abstract class DatabaseProvider implements DataSource{

//      private static final DatabaseProvider instance;
    protected static final Main pl = Main.getInstance();

    private static final Map<Class<? extends DatabaseProvider>, DatabaseProvider> providers = new HashMap<>();

    private static final Settings SETTINGS = Settings.getInstance();

    protected static final SettingsManager settingsManager = SETTINGS.getSettingsManager();

    private static DatabaseProvider currentDatabase;

    protected static final Logger logger = pl.getLogger();

    static
    {
        try{
            providers.put(MySQLDatabase.class, new MySQLDatabase());
        }catch (NoClassDefFoundError ignored){}

        try {
            providers.put(YMLDatabase.class, new YMLDatabase());
        }catch (NoClassDefFoundError ignored){}

        defineCurrentDatabase();
    }

    private static void defineCurrentDatabase(){

        String dataSource = SETTINGS.getSettingsManager().getProperty(DataSourceSettingsHolder.BACKEND);

        if(dataSource.equalsIgnoreCase("mysql")){
            currentDatabase = providers.get(MySQLDatabase.class);
            if(currentDatabase == null){
                throw new NullPointerException("MySQLDatabase class not found.");
            }
        }else if(dataSource.equalsIgnoreCase("yml")){
            currentDatabase = providers.get(YMLDatabase.class);
            if(currentDatabase == null){
                throw new NullPointerException("YMLDatabase class not found.");
            }
        }else{
            //Database by default
            currentDatabase = providers.get(MySQLDatabase.class);
            logger.info("No required database was found. Setting MySQL by default.");
            if(currentDatabase == null){
                throw new NullPointerException("MySQLDatabase class not found.");
            }
        }
    }

    public static DatabaseProvider currentDatabase(){
        return currentDatabase;
    }

    public static DatabaseProvider getProvider(Class<? extends DatabaseProvider> provider){
        return providers.get(provider);
    }

    //================================================//




}
