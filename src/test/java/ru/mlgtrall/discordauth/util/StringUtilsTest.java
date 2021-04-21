package ru.mlgtrall.discordauth.util;


import org.junit.jupiter.api.Test;
import ru.mlgtrall.discordauth.io.database.MySQLDatabase;
import ru.mlgtrall.discordauth.io.database.keys.DataColumns;
import ru.mlgtrall.discordauth.io.database.keys.KeyHolder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void wrapStringArray() {
        String[] testArr = new String[]{"123","key", "jojo"};
        String result = StringUtils.wrapStringArray(testArr, "','", "'", "'");
        assertEquals("'123','key','jojo'", result);
    }

    @Test
    void testWrapStringArray() {
        List<String> testList = new ArrayList<>();
        testList.add("123");
        testList.add("key");
        testList.add("jojo");
        String result = StringUtils.wrapStringArray(testList, "','", "'", "'");
        System.out.println(result);
        assertEquals("'123','key','jojo'", result);
    }

    @Test
    void testWrapKeys() {
        //String result = StringUtils.wrapKeys(DataColumns.values(), ",", "", "");
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
        result = StringUtils.wrapStringArray(arr, delimiter, prefix, postfix);

        assertEquals("'DISCORD_ID','MINE_UUID','REG_IP','LAST_IP','REG_DATE','LAST_DATE','COUNTRY','MINE_NAME','PASSWD_HASH','SALT'",result);

    }
}