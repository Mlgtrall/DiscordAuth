package ru.mlgtrall.jda_bot_bungee_auth.util;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Objects;

/**
 * From javafx + modified
 */
@EqualsAndHashCode
@ToString
public class Pair<K,V> implements Serializable {

    /**
     * Key of this <code>Pair</code>.
     */
    private final K key;

    /**
     * Gets the key for this pair.
     * @return key for this pair
     */
    public K getKey() { return key; }

    /**
     * Value of this this <code>Pair</code>.
     */
    private final V value;

    /**
     * Gets the value for this pair.
     * @return value for this pair
     */
    public V getValue() { return value; }

    /**
     * Creates a new pair
     * @param key The key for this pair
     * @param value The value to use for this pair
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

}

