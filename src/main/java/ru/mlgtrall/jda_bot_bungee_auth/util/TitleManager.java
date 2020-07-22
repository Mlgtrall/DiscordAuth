package ru.mlgtrall.jda_bot_bungee_auth.util;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static ru.mlgtrall.jda_bot_bungee_auth.util.TimeUtil.timeToTicks;

//TODO: make configurable settings
public final class TitleManager {

    private TitleManager(){}


    public static void settings(@NotNull Title title){
        title.fadeIn(timeToTicks(3, TimeUnit.SECONDS));
        title.fadeOut(timeToTicks(3, TimeUnit.SECONDS));
        title.stay(timeToTicks(10, TimeUnit.SECONDS));
    }

    public static void settings(@NotNull Title title, int stay, int fadeIn, int fadeOut, TimeUnit timeUnit){
        title.fadeIn(timeToTicks(fadeIn, timeUnit));
        title.fadeOut(timeToTicks(fadeOut, timeUnit));
        title.stay(timeToTicks(stay, timeUnit));
    }

    public static void send(ProxiedPlayer player, @NotNull String msg, @NotNull Title title){
        title.reset();
        settings(title);
        String[] strings = msg.split(BungeeChatConfig.lineSeparator);
        title.title(new TextComponent(strings[0]));
        for (int i = 1; i<strings.length; ++i){
            title.subTitle(new TextComponent(strings[i]));
        }
        title.send(player);
    }

    public static void send(ProxiedPlayer player, @NotNull String msg){
        Title title = ProxyServer.getInstance().createTitle();
        title.reset();
        settings(title);
        String[] strings = msg.split(BungeeChatConfig.lineSeparator);
        title.title(new TextComponent(strings[0]));
        for (int i = 1; i<strings.length; ++i){
            title.subTitle(new TextComponent(strings[i]));
        }
        title.send(player);
    }

    public static void send(ProxiedPlayer player, @NotNull String msg, int stay, int fadeIn, int fadeOut, TimeUnit timeUnit){
        Title title = ProxyServer.getInstance().createTitle();
        title.reset();
        settings(title, stay, fadeIn, fadeOut, timeUnit);
        String[] strings = msg.split(BungeeChatConfig.lineSeparator);
        title.title(new TextComponent(strings[0]));
        for (int i = 1; i<strings.length; ++i){
            title.subTitle(new TextComponent(strings[i]));
        }
        title.send(player);
    }

}
