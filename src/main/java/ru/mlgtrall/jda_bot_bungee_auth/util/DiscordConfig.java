package ru.mlgtrall.jda_bot_bungee_auth.util;

import ch.jalu.injector.Injector;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee_auth.Main;
import ru.mlgtrall.jda_bot_bungee_auth.bootstrap.InjectorContainer;
import ru.mlgtrall.jda_bot_bungee_auth.io.config.ConfigFileTemplate;
import ru.mlgtrall.jda_bot_bungee_auth.io.FileLoader;


public final class DiscordConfig {

    private static final Main pl = Main.getInstance();
    //private static FileLoader fileLoader = FileLoader.getInstance();
    private static Configuration jdaConfig;
    private static final String separator = "/n";

    private static final FileLoader fileLoader = InjectorContainer.get().getSingleton(FileLoader.class);

    private DiscordConfig(){ }




    public static @Nullable String fromConfig(@NotNull String path){
        //plugin.getLogger().info("DiscordConfig not null");
        jdaConfig = fileLoader.getConfigFile(ConfigFileTemplate.DISCORD_CHAT).getConfig();
        if(jdaConfig == null){
            pl.getLogger().info("jda config is null");
        }else{
            return jdaConfig.getString(path.toUpperCase()).replaceAll(separator, "\n");
        }
        return null;
    }

    public static @NotNull String fromConfig(@NotNull String path, boolean newLine){
        jdaConfig = fileLoader.getConfigFile(ConfigFileTemplate.DISCORD_CHAT).getConfig();
        StringBuilder string;
        if(newLine){
            string = new StringBuilder(jdaConfig.getString(path.toUpperCase()).replaceAll(separator, "\n"));
            string.append('\n');
        }else{
            string = new StringBuilder("\n");
            string.append(jdaConfig.getString(path.toUpperCase()).replaceAll(separator, "\n"));
        }

        return string.toString();
    }
    public static @NotNull String fromConfig(@NotNull String path, String replaceable, String replace){
        jdaConfig = fileLoader.getConfigFile(ConfigFileTemplate.DISCORD_CHAT).getConfig();
        return jdaConfig.getString(path.toUpperCase()).replaceAll(separator, "\n").replaceAll(replaceable, replace);
    }
    public static @NotNull String fromConfig(@NotNull String path, String replaceable, String replace, boolean newLine){
        jdaConfig = fileLoader.getConfigFile(ConfigFileTemplate.DISCORD_CHAT).getConfig();
        StringBuilder string;
        if(newLine) {
            string = new StringBuilder(jdaConfig.getString(path.toUpperCase()).replaceAll(separator, "\n").replaceAll(replaceable, replace));
            string.append('\n');
        }else{
            string = new StringBuilder("\n");
            string.append(jdaConfig.getString(path.toUpperCase()).replaceAll(separator, "\n").replaceAll(replaceable, replace));
        }
        return string.toString();
    }

    public static String fromConfig(@NotNull String path, @NotNull String[] replaceable, @NotNull String[] replace){
        jdaConfig = fileLoader.getConfigFile(ConfigFileTemplate.DISCORD_CHAT).getConfig();
        try {
            if (replaceable.length != replace.length)
                throw new Exception("Replaceable array length isn't equals replace array length");
        }catch (Exception e){
            e.printStackTrace();
        }
        String from = jdaConfig.getString(path.toUpperCase()).replaceAll(separator, "\n");
        String str = "";
        for(int i = 0; i<replaceable.length; ++i){
            str = from.replaceAll(replaceable[i], replace[i]);
        }
        return str;
    }
}
