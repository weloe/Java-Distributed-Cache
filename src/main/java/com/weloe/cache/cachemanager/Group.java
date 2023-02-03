package com.weloe.cache.cachemanager;

import com.weloe.cache.util.BytesUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;


/**
 * @author weloe
 */
public class Group {

    private String name;

    private Cache cache;

    private Getter getter;

    public Group() {

    }

    @FunctionalInterface
    public interface Getter {
        byte[] get(String k) throws Exception;
    }

    public Group(String name, Cache cache, Getter getter) {
        this.name = name;
        this.cache = cache;
        this.getter = getter;
    }

    public CacheObj get(String key) {
        if ("".equals(key) || key == null) {
            throw new RuntimeException("key不能为空");
        }

        CacheObj cacheObj = cache.get(key);

        if (cacheObj != null) {
            return cacheObj;
        }

        return load(key);
    }

    /**
     * 通过Getter回调获取数据
     *
     * @param key
     * @return
     */
    private CacheObj load(String key) {
        byte[] bytes = null;
        try {
            bytes = getter.get(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (bytes == null) {
            return null;
        }
        CacheObj cacheObj = BytesUtil.bytes2CacheObj(bytes);

        cache.add(key, cacheObj);

        return cacheObj;
    }

    public String getName() {
        return name;
    }


    public CacheObj putCacheObj(String key,CacheObj cacheObj){
        CacheObj obj = cache.add(key, cacheObj);
        return obj;
    }

    public LocalDateTime expire(String key, long num, ChronoUnit timeUnit){
        CacheObj cacheObj;
        cacheObj = cache.get(key);
        if(cacheObj == null){
            return null;
        }
        LocalDateTime time = cache.setTTL(key, LocalDateTime.now().plus(num, timeUnit));

        return time;
    }

    public long ttl(String key){
        Duration duration = Duration.between(LocalDateTime.now(), cache.ttl(key));
        return duration.toMillis()/1000;
    }

    public boolean setMaxSize(int num){
        if(num < cache.getNormalByteSize()){
            return false;
        }
        cache.setMaxByteSize(num);
        return true;
    }

    public int getNormalSize(){
        return cache.getNormalByteSize();
    }

    public int getMaxSize(){
        return cache.getMaxByteSize();
    }

    public CacheObj delete(String key){
        CacheObj obj = cache.remove(key);
        return obj;
    }

    public void clear(){
        cache.clear();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public void setGetter(Getter getter) {
        this.getter = getter;
    }
}

