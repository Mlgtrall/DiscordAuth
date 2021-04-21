package ru.mlgtrall.discordauth.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.bootstrap.InjectorContainer;
import ru.mlgtrall.discordauth.io.config.ConfigFileTemplates;
import ru.mlgtrall.discordauth.io.FileLoader;

@Deprecated
public final class BungeeChatConfig {

    private static final FileLoader fileLoader = InjectorContainer.get().getSingleton(FileLoader.class);

    private static final Configuration bungeeConfig = fileLoader.getConfigFile(ConfigFileTemplates.BUNGEE_CHAT).getConfig();

    public static final String lineSeparator = "/n";

    private BungeeChatConfig(){}

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
        return ChatColor.translateAlternateColorCodes('&', fromConfig);
    }

    public static @NotNull TextComponent fromConfig(@NotNull String path){
        TextComponent textComponent = new TextComponent();
        final String fromConfig = bungeeConfig.getString(path.toUpperCase()).replaceAll(lineSeparator,"\n");
        final String fromConfigWithColor = ChatColor.translateAlternateColorCodes('&', fromConfig);
        for(BaseComponent c : TextComponent.fromLegacyText(fromConfigWithColor)) {textComponent.addExtra(c);}
        return textComponent;
    }

    public static @NotNull TextComponent fromConfig(@NotNull String path, String replaceable, String replace, @NotNull Boolean newLine){
        TextComponent textComponent;
        final String fromConfig = bungeeConfig.getString(path.toUpperCase()).replaceAll(replaceable, replace).replaceAll(lineSeparator, "\n");
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

    public static @NotNull TextComponent fromConfig(@NotNull String path, String replaceable, String replace){
        TextComponent textComponent = new TextComponent();
        String fromConfig = bungeeConfig.getString(path.toUpperCase()).replaceAll(replaceable,replace).replaceAll(lineSeparator,"\n");
        String fromConfigWithColor = ChatColor.translateAlternateColorCodes('&', fromConfig);
        for(BaseComponent c: TextComponent.fromLegacyText(fromConfigWithColor)) {
            textComponent.addExtra(c);
        }
        return textComponent;
    }


}
