package com.weloe.cache.outstrategy;

import java.util.*;

/**
 * @author weloe
 */
public class LRUCache<K, V> implements CacheStrategy<K, V> {
    private Map<K, V> map;
    private int capacity;
    private Deque<K> queue;
    private Callback callback;


    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap();
        this.queue = new LinkedList();
    }

    public LRUCache(int capacity,Callback callback) {
        this.capacity = capacity;
        this.map = new HashMap();
        this.queue = new LinkedList();
        this.callback = callback;
    }

    @Override
    public V get(K key) {
        // 如果已经缓存过该数据
        if (map.containsKey(key)) {
            queue.remove(key);
            queue.addFirst(key);
            return map.get(key);
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (map.containsKey(key)) {
            queue.remove(key);
        }
        queue.addFirst(key);
        map.put(key, value);

        // 缓存达到上限
        if (queue.size() > capacity) {
            outCache();
        }
        return value;
    }

    @Override
    public OutEntry<K,V> outCache() {
        // 移除
        K last = queue.removeLast();
        V removeValue = map.remove(last);

        // 回调
        if (callback != null) {
            callback.callback(last, removeValue);
        }
        return new OutEntry<>(last,removeValue);
    }

    @Override
    public V outCache(K k) {
        return remove(k);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void setCallback(Callback<K, V> callback) {
        this.callback = callback;
    }

    @Override
    public List<Object> list() {
        return Arrays.asList(map.entrySet().toArray());
    }


    protected <K> V remove(K key) {
        return map.remove(key);
    }

    protected int size() {
        return map.size();
    }


}
