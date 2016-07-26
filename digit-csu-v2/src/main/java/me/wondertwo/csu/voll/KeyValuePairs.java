package me.wondertwo.csu.voll;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * Created by wondertwo on 2016/7/25.
 */
public class KeyValuePairs {

    private final Map<String, String> params;

    public static KeyValuePairs create() {
        return new KeyValuePairs();
    }

    KeyValuePairs() {
        this.params = new HashMap<>();
    }

    public KeyValuePairs add(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public KeyValuePairs add(String key, int value) {
        this.params.put(key, Integer.toString(value));
        return this;
    }

    public Map<String, String> build() {
        return new HashMap<>(this.params);
    }
}