package ru.mlgtrall.jda_bot_bungee_auth.io.log;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.settings.Settings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConsoleLoggerFactory {

    private static final Map<String, ConsoleLogger> consoleLoggers = new ConcurrentHashMap<>();
    private static Settings settings;

    private ConsoleLoggerFactory(){}

    public static ConsoleLogger get(@NotNull Class<?> owningClass){
        String name = owningClass.getCanonicalName();
        return consoleLoggers.computeIfAbsent(name, ConsoleLoggerFactory::createLogger);
    }

    public static void reloadSettings(Settings settings){
        ConsoleLoggerFactory.settings = settings;
        ConsoleLogger.initSharedSettings(settings);

        consoleLoggers.values().forEach(logger -> logger.initSettings(settings));
    }

    public static int getTotalLoggers(){
        return consoleLoggers.size();
    }

    @Contract(pure = true)
    private static @NotNull ConsoleLogger createLogger(String name){
        ConsoleLogger logger = new ConsoleLogger(name);
        if(settings != null){

        }
        return logger;
    }
}
