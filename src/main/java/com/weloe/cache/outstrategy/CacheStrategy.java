package com.weloe.cache.outstrategy;

import java.util.List;

/**
 * @author weloe
 */
public interface CacheStrategy<K, V> {

    V get(K key);

    V put(K key, V value);

    @FunctionalInterface
    interface Callback<K, V> {
        void callback(K k, V v);
    }

    void setCallback(Callback<K, V> callback);

    List list();

    V outCache();

    V outCache(K k);
}
