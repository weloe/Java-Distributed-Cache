package com.weloe.cache.cachemanager;

import com.weloe.cache.cachemanager.Cache;
import com.weloe.cache.cachemanager.CacheObj;
import com.weloe.cache.outstrategy.CacheStrategy;
import com.weloe.cache.outstrategy.LRUCache;
import com.weloe.cache.util.BytesUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

class CacheTest {
    Cache cache;

    @BeforeEach
    void setUp() {

        CacheStrategy<String, CacheObj> lruCache = new LRUCache<>(5);
        lruCache.setCallback((s1, s2)-> System.out.println("缓存淘汰"));
        cache = new Cache(1024*1024,lruCache);
    }

    @Test
    void add() {
        String s = "123";
        CacheObj cacheObj = new CacheObj(LocalDateTime.MAX, String.class, 512*1024, s.getBytes(StandardCharsets.UTF_8));
        cache.add("test", cacheObj);

        for (int i = 0; i < 5; i++) {
            cache.add("test"+i,cacheObj);
            
        }

    }

    @Test
    void get() {
        CacheObj cacheObj = cache.get("123");
        Assertions.assertNull(cacheObj);

        String s = "123";

        cacheObj = new CacheObj(LocalDateTime.MAX,String.class, s.getBytes(StandardCharsets.UTF_8).length, s.getBytes(StandardCharsets.UTF_8));
        cache.add("test", cacheObj);

        CacheObj test = cache.get("test");
        Assertions.assertNotNull(test);

        byte[] data = test.getData();
        String s1 = BytesUtil.bytes2String(data);

        System.out.println(s1);

    }

    @Test
    void all(){

    }

}