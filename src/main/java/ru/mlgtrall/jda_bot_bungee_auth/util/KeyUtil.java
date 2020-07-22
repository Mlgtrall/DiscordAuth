package ru.mlgtrall.jda_bot_bungee_auth.util;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.KeyHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class KeyUtil {

    public static @NotNull String wrapKeys(KeyHolder @NotNull [] keyArray, String delimiter, String prefix, String postfix){
        int len = keyArray.length;
//        return Arrays.stream(keyArray).map((e) -> e.getContextualKey().getName() + delimiter).reduce((s1, s2) -> s1 +","+ s2).orElse("");
        String[] arr = new String[len];
        for(int i = 0; i < len; i++){
            arr[i] = keyArray[i].getContextualKey().toString();
        }
        return StringUtil.wrapStringArray(arr, delimiter, prefix, postfix);
    }
}
