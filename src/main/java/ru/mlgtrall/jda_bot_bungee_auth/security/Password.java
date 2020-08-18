package ru.mlgtrall.jda_bot_bungee_auth.security;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.mlgtrall.jda_bot_bungee_auth.util.BungeeChatConfig;
import ru.mlgtrall.jda_bot_bungee_auth.util.TitleManager;

public final class Password extends UserStringContract {

    static {
        MIN_LENGTH = 6;
    }

    private Password(){
    }

    //TODO: move to UserStringContract and make more abstract?
    public static @Nullable String returnIfValid(@NotNull String data, ProxiedPlayer player){
        //Allow only allowed symbols for data
        for(char c : data.toCharArray()){
            if(ALLOWED_SYMBOLS.indexOf(c) == -1){
                player.sendMessage(BungeeChatConfig.fromConfig("password_incorrect_char", "<char>", "'"+c+"'",true));
                //TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_reg"));
                return null;
            }
        }

        //Allow only less than max length
        if(data.length()>MAX_LENGTH){
            player.sendMessage(BungeeChatConfig.fromConfig("password_too_long", true));
            //player.sendMessage(chatUtils.fromConfig("join_2"));
            //TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_reg"));
            return null;
        }

        //Allow only more than min length
        if(data.length()<MIN_LENGTH){
            player.sendMessage(BungeeChatConfig.fromConfig("password_too_small", true));
            //TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_reg"));
            return null;
        }
        return data;
    }

    public static boolean checkIfValid(@NotNull String data, ProxiedPlayer player){
        //Allow only allowed symbols for data
        for(char c : data.toCharArray()){
            if(ALLOWED_SYMBOLS.indexOf(c) == -1){
                player.sendMessage(BungeeChatConfig.fromConfig("password_incorrect_char",true));
                //TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_reg"));
                return false;
            }
        }

        //Allow only less than max length
        if(data.length()>MAX_LENGTH){
            player.sendMessage(BungeeChatConfig.fromConfig("password_too_long", true));
            //player.sendMessage(chatUtils.fromConfig("join_2"));
            //TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_reg"));
            return false;
        }

        //Allow only more than min length
        if(data.length()<MIN_LENGTH){
            player.sendMessage(BungeeChatConfig.fromConfig("password_too_small", true));
            //TitleManager.send(player, BungeeChatConfig.fromConfigRaw("title_reg"));
            return false;
        }
        return true;
    }

}
