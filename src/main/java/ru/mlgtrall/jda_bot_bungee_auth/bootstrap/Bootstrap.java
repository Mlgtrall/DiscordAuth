package ru.mlgtrall.jda_bot_bungee_auth.bootstrap;

import ru.mlgtrall.jda_bot_bungee_auth.Main;

import java.util.logging.Logger;

public class Bootstrap {

    private static final Main pl = Main.getInstance();
    private static final Logger logger = pl.getLogger();

    public static void start(){
        calculateDependencies();
        registerUtils();
        startBot();
        registerListeners();
        registerCommands();
    }

    private static void registerUtils(){

    }

    private static void startBot(){

    }

    private static void calculateDependencies(){

    }

    private static void registerCommands(){

    }

    private static void registerListeners(){

    }

    private static void loadDataSource(){

    }
}
