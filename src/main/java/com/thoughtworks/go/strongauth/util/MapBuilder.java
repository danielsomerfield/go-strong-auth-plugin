package com.thoughtworks.go.strongauth.util;

import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {
    private final HashMap<K, V> map;

    public static <KeyType, ValueType> MapBuilder<KeyType, ValueType> create() {
        return new MapBuilder<>();
    }

    private MapBuilder() {
        map = new HashMap<>();
    }

    public MapBuilder<K, V> add(K initialKey, V valueForInitialKey) {
        map.put(initialKey, valueForInitialKey);
        return this;
    }

    public Map<K, V> build() {
        return map;
    }
}
