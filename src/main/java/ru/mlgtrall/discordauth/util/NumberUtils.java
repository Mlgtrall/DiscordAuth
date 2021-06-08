package ru.mlgtrall.discordauth.util;

import java.util.regex.Pattern;

public final class NumberUtils {
    private static final Pattern numberPattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String str){
        if(str == null) return false;
        return numberPattern.matcher(str).matches();
    }
}
