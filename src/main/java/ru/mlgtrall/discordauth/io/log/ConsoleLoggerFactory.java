package ru.mlgtrall.discordauth.io.log;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.bootstrap.InjectorContainer;
import ru.mlgtrall.discordauth.settings.Settings;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConsoleLoggerFactory {

    private static final Map<String, ConsoleLogger> consoleLoggers = new ConcurrentHashMap<>();
    private static Settings settings = InjectorContainer.get().getSingleton(Settings.class);

    private ConsoleLoggerFactory(){}

    public static ConsoleLogger get(@NotNull Class<?> owningClass){
        String name = owningClass.getCanonicalName();
        return consoleLoggers.computeIfAbsent(name, ConsoleLoggerFactory::createLogger);
    }

    public static void reloadSettings(Settings settings){
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
            reloadSettings(settings);
            logger.initSettings(settings);
        }
        return logger;
    }
}
