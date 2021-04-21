package ru.mlgtrall.discordauth.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.Map.Entry;

public final class MapUtils {

    public static <K,V> @NotNull List<V> getValuesList(@NotNull Map<K,V> map){
        List<V> list = new ArrayList<>();
        for(Entry<K,V> entry : map.entrySet()){
            list.add(entry.getValue());
        }
        return list;
    }

    public static <K> Map<K, String> wrapMapValues(@NotNull Map<K, String> map, String prefix, String postfix){
        for (Entry<K, String> entry : map.entrySet()) {
            String val = entry.getValue();
            val = prefix + val + postfix;
            entry.setValue(val);
        }
        return map;
    }

    public static <K,V> @NotNull String asStringPairedSequence(@NotNull Map<K,V> map, String delimiterBetweenElements, String delimiterBetweenPairs, String prefix, String postfix){
        StringBuilder sb = prefix == null ? new StringBuilder() : new StringBuilder(prefix);

        if(delimiterBetweenElements == null){
            delimiterBetweenElements = "";
        }

        if(delimiterBetweenPairs == null){
            delimiterBetweenPairs = "";
        }

        Set<Entry<K, V>> entrySet = map.entrySet();
        Iterator<Entry<K, V>> iterator = entrySet.iterator();
        int size = entrySet.size();
        for(int i = 0; i<size; i++){
            if(iterator.hasNext()){
                Entry<K, V> entry = iterator.next();
                String key = entry.getKey().toString();
                String val = entry.getValue().toString();
                sb.append(key).append(delimiterBetweenElements).append(val);
                if (i != size - 1) {
                    sb.append(delimiterBetweenPairs);
                }
            }
        }

        if(postfix == null){
            postfix = "";
        }

        sb.append(postfix);
        return sb.toString();
    }

}
