package ru.mlgtrall.jda_bot_bungee_auth.util;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.MySQLDatabase;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.DataColumns;
import ru.mlgtrall.jda_bot_bungee_auth.io.database.keys.KeyHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void wrapStringArray() {
        String[] testArr = new String[]{"123","key", "jojo"};
        String result = StringUtil.wrapStringArray(testArr, "','", "'", "'");
        assertEquals("'123','key','jojo'", result);
    }

    @Test
    void testWrapStringArray() {
        List<String> testList = new ArrayList<>();
        testList.add("123");
        testList.add("key");
        testList.add("jojo");
        String result = StringUtil.wrapStringArray(testList, "','", "'", "'");
        System.out.println(result);
        assertEquals("'123','key','jojo'", result);
    }

    @Test
    void testWrapKeys() {
        //String result = StringUtil.wrapKeys(DataColumns.values(), ",", "", "");
        String delimiter = "','";
        String prefix = "'";
        String postfix = "'";
        String result;

        KeyHolder[] keyArray = DataColumns.values();
        int len = keyArray.length;
        String[] arr = new String[len];
        for(int i = 0; i < len; i++){
            arr[i] = keyArray[i].getKey(MySQLDatabase.class).toString();
        }
        result = StringUtil.wrapStringArray(arr, delimiter, prefix, postfix);

        assertEquals("'DISCORD_ID','MINE_UUID','REG_IP','LAST_IP','REG_DATE','LAST_DATE','COUNTRY','MINE_NAME','PASSWD_HASH','SALT'",result);

    }
}