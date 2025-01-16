package kiwiapollo.cobblemontrainerbattle.common;

import java.util.Collection;
import java.util.Set;

public interface ImmutableMap<K, V> {
    V get(K key);

    Set<K> keySet();

    Collection<V> values();
}
