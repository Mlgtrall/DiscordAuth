package ru.mlgtrall.jda_bot_bungee_auth.util;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.KeyHolder;

import java.net.SocketAddress;
import java.util.*;

public final class StringUtil {

    private StringUtil(){}

    public static @NotNull String commaDelimited(@NotNull String... params){
        return wrapStringArray(params, ",", null, null);
    }

    public static @NotNull String wrapStringArray(@NotNull String[] str, String delimiter, String prefix, String postfix){
        StringBuilder newStr = prefix == null ? new StringBuilder() : new StringBuilder(prefix);
        int len = str.length;
        for(int i = 0; i < len; i++){
            newStr.append(str[i]);
            if(i != len - 1){
                newStr.append(delimiter);
            }
        }

        if (postfix != null) {
            newStr.append(postfix);
        }
        return newStr.toString();
    }

    public static @NotNull String wrapStringArray(@NotNull Collection<String> collection, String delimiter, String prefix, String postfix){
        return wrapStringArray(collection.toArray(new String[0]), delimiter, prefix, postfix);
    }

    public static String socketAddressToIp(@NotNull SocketAddress addr){
        return addr.toString().split("/")[1].split(":")[0];
    }

}
