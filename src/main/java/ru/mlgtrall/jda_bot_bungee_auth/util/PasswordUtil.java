package ru.mlgtrall.jda_bot_bungee_auth.util;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PasswordUtil {

    private PasswordUtil(){}

    private static final String ALLOWED_SYMBOLS = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

    public static @Nullable String checkIfValid(@NotNull String password, ProxiedPlayer player){
        //Allow only allowed symbols for password
        for(char c : password.toCharArray()){
            if(!(c >= 'a' && c<= 'z' || c>= 'A' && c<= 'Z' || c>= '0' && c<='9' || c == '_')){
                player.sendMessage(BungeeChatConfig.fromConfig("password_incorrect_char",true));
                TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_reg"));
                return null;
            }
        }

        //Allow only less than max length
        if(password.length()>32){
            player.sendMessage(BungeeChatConfig.fromConfig("password_too_long", true));
            //player.sendMessage(chatUtils.fromConfig("join_2"));
            TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_reg"));
            return null;
        }

        //Allow only more than min length
        if(password.length()<6){
            player.sendMessage(BungeeChatConfig.fromConfig("password_too_small", true));
            //player.sendMessage(chatUtils.fromConfig("join_2"));
            TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_reg"));
            return null;
        }
        return password;
    }

}
