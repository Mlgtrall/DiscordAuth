package ru.mlgtrall.jda_bot_bungee.jda.util;

import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.io.ConfigFiles;
import ru.mlgtrall.jda_bot_bungee.io.FileLoader;


public class JDAConfigUtils {
    private static JDAConfigUtils instance;
    private static Main plugin = Main.getInstance();
    //private static FileLoader fileLoader = FileLoader.getInstance();
    private static Configuration jdaConfig;
    private static final String separator = "/n";

    public static JDAConfigUtils getInstance() {
        return instance;
    }

    public static void setInstance(JDAConfigUtils instance) {
        JDAConfigUtils.instance = instance;
    }

    public JDAConfigUtils(){
        setInstance(this);
        plugin = Main.getInstance();
        //fileLoader = FileLoader.getInstance();
        //jdaConfig = fileLoader.getJDAConfig().getConfig();
        //fileLoader = plugin.getFileLoader();
        //jdaConfig = fileLoader.getJDAConfig().getConfig();

    }




    public static @Nullable String fromConfig(@NotNull String path){
        //plugin.getLogger().info("JDAConfigUtils not null");
        jdaConfig = FileLoader.getInstance().get(ConfigFiles.JDA_CHAT).getConfig();
        if(jdaConfig == null){
            plugin.getLogger().info("jda config is null");
        }else{
            return jdaConfig.getString(path.toUpperCase()).replaceAll(separator, "\n");
        }
        return null;
    }

    public static @NotNull String fromConfig(@NotNull String path, boolean newLine){
        jdaConfig = FileLoader.getInstance().get(ConfigFiles.JDA_CHAT).getConfig();
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
        jdaConfig = FileLoader.getInstance().get(ConfigFiles.JDA_CHAT).getConfig();
        return jdaConfig.getString(path.toUpperCase()).replaceAll(separator, "\n").replaceAll(replaceable, replace);
    }
    public static @NotNull String fromConfig(@NotNull String path, String replaceable, String replace, boolean newLine){
        jdaConfig = FileLoader.getInstance().get(ConfigFiles.JDA_CHAT).getConfig();
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
        jdaConfig = FileLoader.getInstance().get(ConfigFiles.JDA_CHAT).getConfig();
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
