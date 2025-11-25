package kiwiapollo.cobblemontrainerbattle.common;

import java.util.Map;

// TODO do I need this
public interface LazyMap<K, V> {
    V getOrCreate(K key);

    void put(K key, V value);

    void clear();

    void remove(K key);

    Iterable<? extends Map.Entry<K, V>> entrySet();
}
