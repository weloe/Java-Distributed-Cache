package com.weloe.cache.outstrategy;

/**
 * @author weloe
 */
public class LRUKCache<K, V> extends LRUCache<K, V> {

    // 进入缓存队列的评判标准
    private int putSize;

    // 存储访问数据的历史记录
    private LRUCache<Object, Integer> historyList;

    public LRUKCache(int cacheSize, int historyCapacity, int putSize) {
        super(cacheSize);
        this.putSize = putSize;
        this.historyList = new LRUCache(historyCapacity);
    }


    @Override
    public V get(K key) {
        // 记录数据访问次数
        Integer historyCount = historyList.get(key);
        historyCount = historyCount == null ? 0 : historyCount;
        historyList.put(key, ++historyCount);
        return super.get(key);
    }

    @Override
    public V put(K key, V value) {
        if (value == null) {
            return null;
        }
        // 如果已经在缓存里则直接返回
        if (super.get(key) != null) {
            return super.put(key, value);
        }
        // 如果数据历史访问次数达到上限，则加入缓存
        Integer historyCount = historyList.get(key);
        historyCount = (historyCount == null) ? 0 : historyCount;
        if (removeCache(historyCount)) {
            // 移除历史访问记录，加入缓存
            historyList.remove(key);
            return super.put(key, value);
        }

        return value;
    }

    /**
     * 判断是否需要移除历史记录
     * @param historyCount
     * @return
     */
    private boolean removeCache(Integer historyCount) {
        return historyCount >= putSize;
    }


    @Override
    public void setCallback(Callback<K, V> callback) {
        super.setCallback(callback);
    }

    public void setHistoryListCallback(Callback<K, V> callback) {
        historyList.setCallback((Callback<Object, Integer>) callback);
    }

}
