package com.weloe.cache.cachemanager;

import com.weloe.cache.outstrategy.CacheStrategy;

import java.time.LocalDateTime;
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

    // 缓存策略
    private CacheStrategy<String, CacheObj> cacheStrategy;

    Lock readLock;

    Lock writeLock;

    public Cache(int maxByteSize, CacheStrategy<String, CacheObj> cacheStrategy) {
        this.maxByteSize = maxByteSize;
        this.normalByteSize = 0;
        this.cacheStrategy = cacheStrategy;
        readLock = new ReentrantReadWriteLock().readLock();
        writeLock = new ReentrantReadWriteLock().writeLock();
    }

    public CacheObj add(String key, CacheObj cacheObj) {
        writeLock.lock();

        normalByteSize += cacheObj.getByteSize();

        // 缓存上限
        while (normalByteSize > maxByteSize) {
            // 淘汰缓存
            CacheObj outCache = cacheStrategy.outCache();
            normalByteSize -= outCache.getByteSize();
        }

        // 加入缓存
        CacheObj v = cacheStrategy.put(key, cacheObj);

        writeLock.unlock();

        return v;
    }

    public CacheObj get(String key) {
        readLock.lock();

        CacheObj v = cacheStrategy.get(key);
        if (v != null && v.getEndTime() != null && LocalDateTime.now().isAfter(v.getEndTime())) {
            CacheObj obj = cacheStrategy.outCache(key);
            return null;
        }

        readLock.unlock();
        return v;
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
