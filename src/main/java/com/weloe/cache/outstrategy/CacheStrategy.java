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

    OutEntry<K,V> outCache();

    V outCache(K k);

    void clear();

    class OutEntry<K,V>{
        K k;
        V v;

        public OutEntry(K k, V v) {
            this.k = k;
            this.v = v;
        }

        public K getK() {
            return k;
        }

        public V getV() {
            return v;
        }
    }


}
