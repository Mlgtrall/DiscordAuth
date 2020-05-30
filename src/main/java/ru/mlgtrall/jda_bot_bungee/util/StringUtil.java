package ru.mlgtrall.jda_bot_bungee.util;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee.io.database.KeyHolder;

import java.util.Map;
import java.util.Set;

public final class StringUtil {

    private StringUtil(){}

    public static @NotNull Pair<String, String> wrapValues(@NotNull final Set<Map.Entry<KeyHolder, String>> data){
        final String separator = ",";
        final String openingWrapper = "(";
        final String closingWrapper = ")";
        StringBuilder keys = new StringBuilder(openingWrapper);
        StringBuilder values = new StringBuilder(openingWrapper);


        for(Map.Entry<KeyHolder, String> entry : data){
            keys.append("'").append(entry.getKey()).append("'").append(separator);
            values.append("'").append(entry.getValue()).append("'").append(separator);
        }

        keys.deleteCharAt(keys.length()-1);
        values.deleteCharAt(values.length()-1);

        keys.append(closingWrapper);
        values.append(closingWrapper);

        return new Pair<>(keys.toString(), values.toString());
    }

    public static String formatSocketAddressAndGetIP(@NotNull String addr){
        return addr.replaceAll("\\.","_").split(":")[0];
    }

    public static String formatSocketAddressAndGetIP(@NotNull String addr, @NotNull String replacement){
        return addr.replaceAll("\\.", replacement).split(":")[0];
    }

    public static String ipFromSocketAddress(@NotNull String addr){
        return addr.split(":")[0];
    }


}
