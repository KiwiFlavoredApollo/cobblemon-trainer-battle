package kiwiapollo.cobblemontrainerbattle.common;

public interface SimpleMap<K, V> {
    V get(K key);

    void put(K key, V value);

    boolean containsKey(K key);
}
