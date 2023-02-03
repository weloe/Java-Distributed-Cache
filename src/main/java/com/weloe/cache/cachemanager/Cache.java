package com.weloe.cache.cachemanager;

import com.weloe.cache.outstrategy.CacheStrategy;
import com.weloe.cache.outstrategy.LRUCache;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author weloe
 */
public class Cache {
    // 最大字节
    private int maxByteSize;

    // 目前使用字节
    private int normalByteSize;

    // 缓存策略，存储数据
    private CacheStrategy<String, CacheObj> cacheStrategy;

    // 存储key和过期时间
    private Map<String,LocalDateTime> timeMap;

    Lock readLock;

    Lock writeLock;

    public Cache(){
        this.maxByteSize = 1024*1024;
        this.normalByteSize = 0;
        this.cacheStrategy = new LRUCache<>(10000);
        readLock = new ReentrantReadWriteLock().readLock();
        writeLock = new ReentrantReadWriteLock().writeLock();
        timeMap = new LinkedHashMap<>();
    }

    public Cache(int maxByteSize, CacheStrategy<String, CacheObj> cacheStrategy) {
        this.maxByteSize = maxByteSize;
        this.normalByteSize = 0;
        this.cacheStrategy = cacheStrategy;
        readLock = new ReentrantReadWriteLock().readLock();
        writeLock = new ReentrantReadWriteLock().writeLock();
        timeMap = new LinkedHashMap<>();
    }

    public CacheObj add(String key, CacheObj cacheObj) {
        writeLock.lock();

        normalByteSize += cacheObj.getByteSize();

        // 缓存上限
        while (normalByteSize > maxByteSize) {
            // 淘汰缓存
            CacheStrategy.OutEntry<String,CacheObj> outCache = cacheStrategy.outCache();
            timeMap.remove(outCache.getK());
            normalByteSize -= outCache.getV().getByteSize();
        }

        // 加入缓存
        CacheObj v = cacheStrategy.put(key, cacheObj);

        writeLock.unlock();

        return v;
    }

    public CacheObj get(String key) {
        readLock.lock();

        CacheObj v = cacheStrategy.get(key);
        LocalDateTime time = timeMap.get(key);
        if (v != null &&  time!= null && LocalDateTime.now().isAfter(time)) {
            CacheStrategy.OutEntry<String,CacheObj> outCache = cacheStrategy.outCache();
            timeMap.remove(outCache.getK());
            normalByteSize -= outCache.getV().getByteSize();
            return null;
        }

        readLock.unlock();
        return v;
    }

    public LocalDateTime setTTL(String key,LocalDateTime time){
        timeMap.put(key, time);
        return time;
    }

    public LocalDateTime ttl(String key){
        return timeMap.get(key);
    }

    public CacheObj remove(String key){
        return cacheStrategy.outCache(key);
    }

    public void clear(){
        cacheStrategy.clear();
    }

    public void setMaxByteSize(int maxByteSize) {
        this.maxByteSize = maxByteSize;
    }

    public int getMaxByteSize() {
        return maxByteSize;
    }

    public int getNormalByteSize() {
        return normalByteSize;
    }

}
