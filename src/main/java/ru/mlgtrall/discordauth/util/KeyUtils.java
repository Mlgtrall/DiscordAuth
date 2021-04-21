package ru.mlgtrall.discordauth.util;

import org.jetbrains.annotations.NotNull;
import ru.mlgtrall.discordauth.io.database.keys.KeyHolder;

public final class KeyUtils {

    public static @NotNull String wrapKeys(KeyHolder @NotNull [] keyArray, String delimiter, String prefix, String postfix){
        int len = keyArray.length;
//        return Arrays.stream(keyArray).map((e) -> e.getContextualKey().getName() + delimiter).reduce((s1, s2) -> s1 +","+ s2).orElse("");
        String[] arr = new String[len];
        for(int i = 0; i < len; i++){
            arr[i] = keyArray[i].getContextualKey().toString();
        }
        return StringUtils.wrapStringArray(arr, delimiter, prefix, postfix);
    }
}
