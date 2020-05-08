package ru.mlgtrall.jda_bot_bungee.bungee.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.io.ConfigFiles;
import ru.mlgtrall.jda_bot_bungee.io.FileLoader;

public class ChatManager {
    private static Main pl = Main.getInstance();
    private static FileLoader fileLoader = FileLoader.getInstance();
    private static Configuration bungeeConfig = fileLoader.get(ConfigFiles.BUNGEE_CHAT).getConfig();
    public static final String lineSeparator = "/n";
    public ChatManager(){
        pl = Main.getInstance();
        fileLoader = FileLoader.getInstance();
        bungeeConfig = fileLoader.get(ConfigFiles.BUNGEE_CHAT).getConfig();
    }

    @NotNull
    public static String getStringWithColors (String s){
        return ChatColor.translateAlternateColorCodes('&',s);
    }

    public static @NotNull TextComponent fromConfig(@NotNull String path, @NotNull Boolean newLine){
        TextComponent textComponent;
        final String fromConfig = bungeeConfig.getString(path.toUpperCase()).replaceAll(lineSeparator,"\n");
        final String fromConfigWithColor = ChatColor.translateAlternateColorCodes('&', fromConfig);
        if(newLine) {
            textComponent = new TextComponent("\n");
            for(BaseComponent c: TextComponent.fromLegacyText(fromConfigWithColor)) {
                textComponent.addExtra(c);
            }
        }else{
            textComponent = new TextComponent();
            for(BaseComponent c: TextComponent.fromLegacyText(fromConfigWithColor)) {
                textComponent.addExtra(c);
            }
        }

        return textComponent;
    }

    public static @NotNull String fromConfigRaw(@NotNull String path){
        final String fromConfig = bungeeConfig.getString(path.toUpperCase());
        final String fromConfigWithColors = ChatColor.translateAlternateColorCodes('&', fromConfig);
        return fromConfigWithColors;
    }

    public static @NotNull TextComponent fromConfig(@NotNull String path){
        TextComponent textComponent = new TextComponent();
        final String fromConfig = bungeeConfig.getString(path.toUpperCase()).replaceAll(lineSeparator,"\n");
        final String fromConfigWithColor = ChatColor.translateAlternateColorCodes('&', fromConfig);
        for(BaseComponent c : TextComponent.fromLegacyText(fromConfigWithColor)) {textComponent.addExtra(c);}
        return textComponent;
    }

    public static @NotNull TextComponent fromConfig(@NotNull String path, String replaceble, String replace, @NotNull Boolean newLine){
        TextComponent textComponent;
        final String fromConfig = bungeeConfig.getString(path.toUpperCase()).replaceAll(replaceble, replace).replaceAll(lineSeparator, "\n");
        final String fromConfigWithColor = ChatColor.translateAlternateColorCodes('&', fromConfig);
        if(newLine){
            textComponent = new TextComponent("\n");
            for(BaseComponent c: TextComponent.fromLegacyText(fromConfigWithColor)) {
                textComponent.addExtra(c);
            }
        }else {
            textComponent = new TextComponent();
            for(BaseComponent c: TextComponent.fromLegacyText(fromConfigWithColor)) {
                textComponent.addExtra(c);
            }
        }
         return textComponent;
    }

    public static @NotNull TextComponent fromConfig(@NotNull String path, String replaceble, String replace){
        TextComponent textComponent = new TextComponent();
        String fromConfig = bungeeConfig.getString(path.toUpperCase()).replaceAll(replaceble,replace).replaceAll(lineSeparator,"\n");
        String fromConfigWithColor = ChatColor.translateAlternateColorCodes('&', fromConfig);
        for(BaseComponent c: TextComponent.fromLegacyText(fromConfigWithColor)) {
            textComponent.addExtra(c);
        }
        return textComponent;
    }


}
