package ru.mlgtrall.jda_bot_bungee.security;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee.Main;
import ru.mlgtrall.jda_bot_bungee.bungee.util.ChatManager;
import ru.mlgtrall.jda_bot_bungee.bungee.util.TitleManager;

public class Password {
    public static @Nullable String checkIfValid(@NotNull String password, ProxiedPlayer player){
        //Allow only allowed symbols for password
        for(char c : password.toCharArray()){
            if(!(c >= 'a' && c<= 'z' || c>= 'A' && c<= 'Z' || c>= '0' && c<='9' || c == '_')){
                player.sendMessage(ChatManager.fromConfig("password_incorrect_char",true));
                TitleManager.send(player, ChatManager.fromConfigRaw("title_reg"));
                return null;
            }
        }

        //Allow only less than max length
        if(password.length()>32){
            player.sendMessage(ChatManager.fromConfig("password_too_long", true));
            //player.sendMessage(chatUtils.fromConfig("join_2"));
            TitleManager.send(player, ChatManager.fromConfigRaw("title_reg"));
            return null;
        }

        //Allow only more than min length
        if(password.length()<6){
            player.sendMessage(ChatManager.fromConfig("password_too_small", true));
            //player.sendMessage(chatUtils.fromConfig("join_2"));
            TitleManager.send(player, ChatManager.fromConfigRaw("title_reg"));
            return null;
        }
        return password;
    }

}
